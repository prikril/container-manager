package api.base;

import application.GlobalConfiguration;
import application.logging.LoggingFacade;
import okhttp3.OkHttpClient;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import javax.naming.ConfigurationException;
import javax.net.ssl.*;
import javax.xml.bind.DatatypeConverter;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class UnsafeOkHttpClient {

    private static LoggingFacade logger = LoggingFacade.getInstance();

    public static OkHttpClient getUnsafeOkHttpClient(int timeoutInSeconds) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(getKeyManagers(), trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.readTimeout(timeoutInSeconds, TimeUnit.SECONDS).build();
            return okHttpClient;
        } catch (Exception e) {
            logger.log("Error in certificate process. Aborting.");
            throw new RuntimeException(e);
        }
    }

    private static KeyManager[] getKeyManagers() {

        KeyManagerFactory kmf = null;
        try {
            kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(getKeyStore(readCertFileToString(), readKeyFileToString()), TEMPORARY_KEY_PASSWORD.toCharArray());
            KeyManager[] keyManagers = kmf.getKeyManagers();
            return keyManagers;
        } catch (NoSuchAlgorithmException | ConfigurationException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String readCertFileToString() {
        final String CERT_PATH = GlobalConfiguration.getInstance().getCertPath();
        String certContent = "";

        try {
            certContent = readFileToString(CERT_PATH);
        } catch (IOException e) {
            logger.log("Cannot read certificate from: " + CERT_PATH);
            e.printStackTrace();
        }

        return certContent;
    }

    private static String readKeyFileToString() {
        final String KEYFILE_PATH = GlobalConfiguration.getInstance().getKeyfilePath();
        String keyContent = "";

        try {
            keyContent = readFileToString(KEYFILE_PATH);
        } catch (IOException e) {
            logger.log("Cannot read keyfile from: " + KEYFILE_PATH);
            e.printStackTrace();
        }

        return keyContent;
    }

    private static String readFileToString(String filePath) throws IOException {
        // https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file
        BufferedReader reader = new BufferedReader(new FileReader (filePath));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

    // https://futurestud.io/tutorials/retrofit-2-how-to-trust-unsafe-ssl-certificates-self-signed-expired
    // https://github.com/square/okhttp/blob/c77023cd0369c679bf217506ae21d429e6e81bdf/okhttp-tests/src/test/java/okhttp3/internal/tls/ClientAuthTest.java#L108
    // https://stackoverflow.com/questions/42675033/how-to-build-a-sslsocketfactory-from-pem-certificate-and-key-without-converting
    // https://stackoverflow.com/questions/12501117/programmatically-obtain-keystore-from-pem?rq=1

    private static final String TEMPORARY_KEY_PASSWORD = "changeit";

    private static KeyStore getKeyStore(String certificatePem, String privateKeyPem) throws ConfigurationException {
        try {
            Certificate clientCertificate = loadCertificate(certificatePem);
            PrivateKey privateKey = loadPrivateKey(privateKeyPem);
            //Certificate caCertificate = loadCertificate(caPem);

            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(null, null);
            //keyStore.setCertificateEntry("ca-cert", caCertificate);
            keyStore.setCertificateEntry("client-cert", clientCertificate);
            keyStore.setKeyEntry("client-key", privateKey, TEMPORARY_KEY_PASSWORD.toCharArray(), new Certificate[]{clientCertificate});
            return keyStore;
        } catch (GeneralSecurityException | IOException e) {
            throw new ConfigurationException("Cannot build keystore: " + e);
        }
    }

    private static Certificate loadCertificate(String certificatePem) throws IOException, GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
        //final byte[] content = readPemContent(certificatePem);
        byte[] certBytes = parseDERFromPEM(certificatePem.getBytes(), "-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----");
        return certificateFactory.generateCertificate(new ByteArrayInputStream(certBytes));
    }

    private static PrivateKey loadPrivateKey(String privateKeyPem) throws IOException, GeneralSecurityException {
        return pemLoadPrivateKeyPkcs1OrPkcs8Encoded(privateKeyPem);
    }

    private static PrivateKey pemLoadPrivateKeyPkcs1OrPkcs8Encoded(
            String privateKeyPem) throws GeneralSecurityException, IOException {
        // PKCS#8 format
        final String PEM_PRIVATE_START = "-----BEGIN PRIVATE KEY-----";
        final String PEM_PRIVATE_END = "-----END PRIVATE KEY-----";

        // PKCS#1 format
        final String PEM_RSA_PRIVATE_START = "-----BEGIN RSA PRIVATE KEY-----";
        final String PEM_RSA_PRIVATE_END = "-----END RSA PRIVATE KEY-----";

        if (privateKeyPem.contains(PEM_PRIVATE_START)) { // PKCS#8 format
            privateKeyPem = privateKeyPem.replace(PEM_PRIVATE_START, "").replace(PEM_PRIVATE_END, "");
            privateKeyPem = privateKeyPem.replaceAll("\\s", "");

            byte[] pkcs8EncodedKey = Base64.getDecoder().decode(privateKeyPem);

            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(new PKCS8EncodedKeySpec(pkcs8EncodedKey));

        } else if (privateKeyPem.contains(PEM_RSA_PRIVATE_START)) {  // PKCS#1 format

            privateKeyPem = privateKeyPem.replace(PEM_RSA_PRIVATE_START, "").replace(PEM_RSA_PRIVATE_END, "");
            privateKeyPem = privateKeyPem.replaceAll("\\s", "");

            DerInputStream derReader = new DerInputStream(Base64.getDecoder().decode(privateKeyPem));

            DerValue[] seq = derReader.getSequence(0);

            if (seq.length < 9) {
                throw new GeneralSecurityException("Could not parse a PKCS1 private key.");
            }

            // skip version seq[0];
            BigInteger modulus = seq[1].getBigInteger();
            BigInteger publicExp = seq[2].getBigInteger();
            BigInteger privateExp = seq[3].getBigInteger();
            BigInteger prime1 = seq[4].getBigInteger();
            BigInteger prime2 = seq[5].getBigInteger();
            BigInteger exp1 = seq[6].getBigInteger();
            BigInteger exp2 = seq[7].getBigInteger();
            BigInteger crtCoef = seq[8].getBigInteger();

            RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2,
                    exp1, exp2, crtCoef);

            KeyFactory factory = KeyFactory.getInstance("RSA");

            return factory.generatePrivate(keySpec);
        }

        throw new GeneralSecurityException("Not supported format of a private key");
    }



    protected static byte[] parseDERFromPEM(byte[] pem, String beginDelimiter, String endDelimiter) {
        String data = new String(pem);
        String[] tokens = data.split(beginDelimiter);
        tokens = tokens[1].split(endDelimiter);
        return DatatypeConverter.parseBase64Binary(tokens[0]);
    }

    protected static RSAPrivateKey generatePrivateKeyFromDER(byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory factory = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey)factory.generatePrivate(spec);
    }

    protected static X509Certificate generateCertificateFromDER(byte[] certBytes) throws CertificateException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");

        return (X509Certificate)factory.generateCertificate(new ByteArrayInputStream(certBytes));
    }

}
