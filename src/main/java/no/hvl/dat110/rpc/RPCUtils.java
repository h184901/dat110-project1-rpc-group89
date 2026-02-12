package no.hvl.dat110.rpc;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import no.hvl.dat110.TODO;

public class RPCUtils {
	
	public static byte[] encapsulate(byte rpcid, byte[] payload) {
		
		byte[] rpcmsg = null;
		
		// TODO - START
        // dersom metoden har void parameter / returverdi
        if(payload==null){
            payload = new byte[0];
        }

        rpcmsg = new byte[1+payload.length];
        rpcmsg[0] = rpcid;

        System.arraycopy(payload,0,rpcmsg,1,payload.length);
        return rpcmsg;
        //TODO - END
	}
	
	public static byte[] decapsulate(byte[] rpcmsg) {
		
		byte[] payload = null;
		
		// TODO - START

        if(rpcmsg == null || rpcmsg.length < 1){
            throw new IllegalArgumentException("Invalid rpc message");
        }
		// Decapsulate the rpcid and payload in a byte array according to the RPC message syntax

        payload = Arrays.copyOfRange(rpcmsg, 1, rpcmsg.length);

		// TODO - END
		
		return payload;
		
	}

	// convert String to byte array
    public static byte[] marshallString(String str) {
        if (str == null) {
            throw new IllegalArgumentException("String is null");
        }
        byte[] encoded = str.getBytes(StandardCharsets.UTF_8);

        // RPC payload må være <= 127-1 hvis du også har rpcid? (RPCUtils.encapsulate legger rpcid i egen byte)
        // Men selve payloaden i RPC kan fortsatt være <=127, og message-laget håndterer <=127 total payload.
        if (encoded.length > 127) {
            throw new IllegalArgumentException("String too long (bytes): " + encoded.length);
        }
        return encoded;
    }

    public static String unmarshallString(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("data is null");
        }
        return new String(data, StandardCharsets.UTF_8);
    }
	
	public static byte[] marshallVoid() {
		
		byte[] encoded = null;
        encoded = new byte[0];
		return encoded;
		
	}
	
	public static void unmarshallVoid(byte[] data) {

	}

	// convert boolean to a byte array representation
	public static byte[] marshallBoolean(boolean b) {
		
		byte[] encoded = new byte[1];
				
		if (b) {
			encoded[0] = 1;
		} else
		{
			encoded[0] = 0;
		}
		
		return encoded;
	}

	// convert byte array to a boolean representation
	public static boolean unmarshallBoolean(byte[] data) {
		
		return (data[0] > 0);
		
	}

	// integer to byte array representation
	public static byte[] marshallInteger(int x) {
		
		byte[] encoded = null;
		
		// TODO - START 

        encoded = ByteBuffer.allocate(4).putInt(x).array();

		// TODO - END
		
		return encoded;
	}
	
	// byte array representation to integer
	public static int unmarshallInteger(byte[] data) {
		
		int decoded = 0;
		
		// TODO - START 
        if(data == null || data.length != 4){
            throw new IllegalArgumentException("Invalid data for int");
        }
        decoded = ByteBuffer.wrap(data).getInt();
		// TODO - END
		
		return decoded;
		
	}
}
