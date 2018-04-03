package coms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.Arrays;


public class Messge {
	//compos d'un message
	private int id;
	private int userId;
	private int salleId;
	private String content;
	
	//Nouveau message
	public Messge(int id, int userId, int salleId, String content) {
		this.id = id;
		this.userId= userId;
		this.salleId = salleId;
		this.content = content;
	}
	
	//Reconstruction à partir d'un byte[] reçu. Pourrait être plus intelligent un peu mais ça marche
	public Messge(byte[] by) {
		//le byte[] contient 4 section - 3 ids et le message lui-même.
		//on définit les index des limites de chaque block
		int limit1 = Utils.messageIdBytes;
		int limit2 = Utils.authorIdBytes + Utils.messageIdBytes;
		int limit3 = Utils.authorIdBytes + Utils.messageIdBytes + Utils.salleIdBytes;
		
		//Casser le byte[] en plusieurs sous-byte[] et parser en int, String.
		this.id = Utils.bytesToInt(Arrays.copyOfRange(by, 0, limit1));
		this.userId= Utils.bytesToInt(Arrays.copyOfRange(by, limit1, limit2));
		this.salleId = Utils.bytesToInt(Arrays.copyOfRange(by, limit2, limit3));
		this.content = new String(Arrays.copyOfRange(by, limit3, by.length));
	}
	
	
	public byte[] toBytes() throws IOException {
		/*Convertir tout le msg en bytes pour envoie*/
		byte byteRep[] = merge4Arrays(Utils.intTo4Bytes(id), Utils.intTo4Bytes(userId), 
				Utils.intTo4Bytes(salleId), content.getBytes());
		
		return byteRep;
	}
	
	public byte[] merge4Arrays(byte[] a, byte[] b,byte[] c,byte[] d) throws IOException {
		/*Un peu paresseux, devrait être itératif pour être plus général... si on change pas les headers c'Est ok
		 * Sinon faudrait ajouter des outputStream.write() si on veut plus de 4 arguemnts*/
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		outputStream.write( a );
		outputStream.write( b );
		outputStream.write( c );
		outputStream.write( d );
		byte e[] = outputStream.toByteArray();
		return e;
	}
	
	public String toString() {
		return "ID:" + Integer.toString(id) + " author:"+ Integer.toString(userId) + " salleId:"+ Integer.toString(salleId) + " " + content;
	}
	
	public int getId() {
		return id;
	}

	public int getuserId() {
		return userId;
	}

	public int getSalleId() {
		return salleId;
	}

	public String getContent() {
		return content;
	}

}