package com.ex;


import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class RsaExampleApplication {

	public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, IOException, InvalidKeyException {
		RSAService rsaService=new RSAService();
		//File file=new File("src/main/resources/test.txt");
		while (true){
			Scanner scanner=new Scanner(System.in);
			System.out.println("Select option: ");
			System.out.println("1 - generate keys");
			System.out.println("2 - encode file");
			System.out.println("3 - decode file");
			System.out.println("0 - exit");
			int opt=scanner.nextInt();
			switch (opt){
				case 1 ->{
					System.out.println("Enter keyfile location: ");
					scanner.nextLine();
					String location=scanner.nextLine();
					System.out.println(rsaService.generateKeys(location));
				}
				case 2 ->{
					System.out.print("Enter file location: ");
					scanner.nextLine();
					String location=scanner.nextLine();
					System.out.print("Enter key: ");
					String key=scanner.nextLine();
					rsaService.encode(new File(location), key);
				}
				case 3 ->{
					System.out.print("Enter file location: ");
					scanner.nextLine();
					String location=scanner.nextLine();
					System.out.print("Enter key location: ");
					String key=scanner.nextLine();
					rsaService.decode(new File(location), key);
				}
				case 0 ->{
					return;
				}
			}
		}
	}

}
