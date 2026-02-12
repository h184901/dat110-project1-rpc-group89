package no.hvl.dat110.messaging;

import java.util.Arrays;

import no.hvl.dat110.TODO;

public class MessageUtils {

	public static final int SEGMENTSIZE = 128;

	public static int MESSAGINGPORT = 8080;
	public static String MESSAGINGHOST = "localhost";

	public static byte[] encapsulate(Message message) {
		

		// TODO - START
		
		// encapulate/encode the payload data of the message and form a segment
		// according to the segment format for the messaging layer
        byte[] segment = new byte[SEGMENTSIZE];
        byte[] data;

        if(message == null){
            throw new IllegalArgumentException("message cannot be null");
        }

        data = message.getData();

        if(data == null){
            throw new IllegalArgumentException("message data cannot be null");
        }

        if(data.length>127){
            throw new IllegalArgumentException("message data too long: " + data.length);
        }

        segment[0] = (byte) data.length;
        System.arraycopy(data, 0, segment, 1, data.length);

		// TODO - END
		return segment;
		
	}

	public static Message decapsulate(byte[] segment) {

		Message message = null;
		
		// TODO - START
		// decapsulate segment and put received payload data into a message
        if(segment == null){
            throw new IllegalArgumentException("segment cannot be null");
        }
        if(segment.length != SEGMENTSIZE){
            throw new IllegalArgumentException("segment must be " + SEGMENTSIZE + " bytes");
        }
        int length = Byte.toUnsignedInt(segment[0]);
        if(length>127){
            throw new IllegalArgumentException("invalid payload length in header: " + length);
        }
        byte[] data = Arrays.copyOfRange(segment, 1, 1+length);
        message = new Message(data);
		// TODO - END

		return message;
		
	}
	
}
