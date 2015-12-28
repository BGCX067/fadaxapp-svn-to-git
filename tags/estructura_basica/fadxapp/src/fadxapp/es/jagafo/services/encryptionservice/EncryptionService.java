package fadxapp.es.jagafo.services.encryptionservice;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import fadxapp.es.jagafo.controller.exceptions.ServiceException;
import fadxapp.es.jagafo.view.Activator;

public class EncryptionService implements IEncryptionService {
	public final static String ENCODING = "UTF.8"; 
	protected static final String secretPhrase = Activator.PLUGIN_ID+"key";
	
	private SecretKey skey = null;
	private SecretKeySpec skeySpec = null;
	private Cipher cipher = null;
	private boolean init = false;
	private CifradorDES cifrador;

	public EncryptionService() {
		init = prepareEncode();
	}

	private boolean prepareEncode() {
		try {
			cifrador = new CifradorDES();
		} catch (Exception e) {
			new ServiceException(this,
				"Error al instanciar la clase de encriptacion", e);
			return false;
		}
		return true;
	}

	protected boolean prepareBlowFishEncDec() {
		try {
			skey = SecretKeyFactory.getInstance("Blowfish")
					.generateSecret(
							new javax.crypto.spec.PBEKeySpec(secretPhrase
									.toCharArray()));
		} catch (NoSuchAlgorithmException nsae) {
			new ServiceException(this,
					"KeyGenerator(Blowfish) algorithm not found", nsae);
			return false;
		} catch (InvalidKeySpecException e) {
			new ServiceException(this,
					"KeyGenerator(Blowfish) key not generated", e);
			return false;
		}
		byte[] raw = skey.getEncoded();
		skeySpec = new SecretKeySpec(raw, "Blowfish");
		try {
			cipher = Cipher.getInstance("Blowfish");
		} catch (NoSuchAlgorithmException nsae) {
			new ServiceException(this,
					"Cipher(Blowfish) algorithm not found", nsae);
			return false;
		} catch (NoSuchPaddingException nspe) {
			new ServiceException(this,
					"Cipher(Blowfish) padding not found", nspe);
			return false;
		}
		return true;
	}

	/**
	 * @param sDataToEncrypt
	 *            - String that needs to be encrypted.
	 * @return String - encrypted string
	 */
	protected String encryptWithBlowfishKey(String sDataToEncrypt)
			throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(sDataToEncrypt.getBytes());
		for (int i = 0; i < encrypted.length; ++i) {
			System.out.print(encrypted[i]);
		}
		return new String(encrypted);
	}

	/**
	 * @param sDataToDecrypt
	 *            - String that needs to be decrypted.
	 * @return String - decrypted string
	 */
	protected String decryptWithBlowfishKey(String sDataToDecrypt)
			throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] encrypted = sDataToDecrypt.getBytes();
		byte[] decrypted = cipher.doFinal(encrypted);
		for (int i = 0; i < decrypted.length; ++i) {
			System.out.print(decrypted[i]);
		}
		String decryptedString = new String(decrypted);
		return new String(decryptedString);
	}

	protected String encryptBase64(String value) {
		return Base64.encodeBase64String(value.getBytes());
	}

	protected String decryptBase64(String value) {
		return new String(Base64.decodeBase64(value));
	}
	
	protected String encryptDES(String value){
		if (init)
			try {
				return cifrador.cifrar(value);
			} catch (Exception e) {
				new ServiceException(this,"Error al cifrar "+value, e);
				return value;
			}
		return value;
	}
	
	protected String decryptDES(String value){
		if (init){
			try {
				return cifrador.descifrar(value);
			} catch (Exception e) {
				new ServiceException(this,"Error al descifrar "+value, e);
				return value;
			}
		}
		return value;
	}

	public String encrypt(String value) {
		if (init) {
			try {
				return encryptDES(value);
			} catch (Exception e) {
				new ServiceException(
					this,
					"Error mientras se encriptaba", e);
			}
		}
		return value;
	}

	public String decrypt(String value) {
		if (init) {
			try {
				return decryptDES(value);
			} catch (Exception e) {
				new ServiceException(this,
				 "Error mientras se desencriptaba", e);
			}
		}
		return value;
	}

	public boolean isInitialize() {
		return init;
	}
}

class CifradorDES {

    byte[] buf = new byte[1024];
    Cipher ecipher;
    Cipher dcipher;

    private final static String CABECERA_CONVERSION_HEXA_TO_BYTE = "HEXA_BYTE?";  // No cambiar NUNCA la cabecera
    
    private static SecretKey getKey() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        final byte[] semilla = (EncryptionService.secretPhrase).getBytes();
        SecureRandom sr = new SecureRandom(semilla);
        KeyGenerator kGen = KeyGenerator.getInstance("DESEDE");
        kGen.init(sr);
        return kGen.generateKey();
    }
    
    
    CifradorDES() throws Exception {        
        this(getKey());        
    }

    
    CifradorDES(SecretKey key) throws Exception {
        ecipher = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
        dcipher = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, key);
        dcipher.init(Cipher.DECRYPT_MODE, key);
    }
    
    public String cifrar(String str) throws Exception {
        // Convierte el String a cifrar en un array de bytes usando el encoding ISO-8859-15
        byte[] bytes = str.getBytes(EncryptionService.ENCODING);
        // cifra el array de bytes
        byte[] bytesCifrados = ecipher.doFinal(bytes);
        // Convierte la salida cifrada en binario a un string de enteros
       return byteToHexa(bytesCifrados, str);
    }
    
    public String descifrar(String str) throws Exception {               
        // Convertimos el texto en un array de bytes
        byte[] bytes = hexaToByte(str);        
        // Descifrar
        byte[] bytesDesc = dcipher.doFinal(bytes);
        // Codifica los bytes al encoding ISO-8859-15 para obtener el texto descifrado
        return new String(bytesDesc, EncryptionService.ENCODING);
    }
    
    private String byteToHexa(byte[] bytesCifrados, String str) throws Exception {
        StringBuffer hexData = new StringBuffer();
        hexData.append(CABECERA_CONVERSION_HEXA_TO_BYTE);
        for (int i = 0; i < bytesCifrados.length; i++) {
            String resultadoHex = Integer.toHexString(0xff & bytesCifrados[i]);
            if (resultadoHex.length() < 2) {
                // Los bytes son dos dígitos y si se convierte a un entero menor que nueve, se pierde un dígito
                resultadoHex = "0" + resultadoHex;
            }
            hexData.append(resultadoHex);
            Byte checkConversion = Integer.decode("0xff" + resultadoHex).byteValue();
            if (checkConversion != bytesCifrados[i]) {
                throw new Exception("Ha fallado la conversión a ASCII de los datos binarios resultantes de cifrar el texto " + str
                        + ". En la conversión de : " + bytesCifrados[i] + " ha generado: " + checkConversion + " cuando debería haber generado: " + resultadoHex);
            }
        }
        return hexData.toString();
    }
    
    
    private byte[] hexaToByte(String str) throws UnsupportedEncodingException {
        byte[] bytes;
        // Si el texto que recibimos, tiene la cabecera, se convierte el array de hexadecimales a un array de bytes
        if (str.startsWith(CABECERA_CONVERSION_HEXA_TO_BYTE)) {
            str = str.substring(CABECERA_CONVERSION_HEXA_TO_BYTE.length());
            bytes = new byte[(str.length()/2)];
            for (int i = 0; i < str.length(); i = i+2) {
                bytes[i/2] = Integer.decode("0xff" + str.substring(i, i+2)).byteValue();
            }
        } else {
            bytes = str.getBytes(EncryptionService.ENCODING);
        }
        return bytes;
    }
}
