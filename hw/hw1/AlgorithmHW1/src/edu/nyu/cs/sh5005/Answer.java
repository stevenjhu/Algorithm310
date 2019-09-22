package edu.nyu.cs.sh5005;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class Answer {

	public static void main(String[] args) throws UnsupportedEncodingException,IOException {
		   BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out,"ASCII"),4096);
		   BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		   TwoThreeTree tree = new TwoThreeTree();
		   
		   int num_planet = Integer.parseInt(input.readLine());
		   for(int i = 0; i< num_planet; i++) {
			   String[] list = input.readLine().split(" ");
			   String key = list[0];
			   int value = Integer.parseInt(list[1]);
			   twothree.insert(key,value,tree);
		   }
		   
		   int query = Integer.parseInt(input.readLine());
		   Keys[] chain = new Keys[query];
		   
		   
		   for(int i = 0; i< query; i++) {
			   String[] key_planets = input.readLine().split(" ");
			   chain[i] = new Keys();
			   chain[i].key = key_planets;
		   }
		   for(Keys key: chain) {
			   twothree.printKeyRange((InternalNode)tree.root,key.key[0],key.key[1],tree.height,output);
		   }
		   
		   
		   output.flush();
	   }

}
