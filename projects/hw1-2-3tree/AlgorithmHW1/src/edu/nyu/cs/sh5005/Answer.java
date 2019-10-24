package edu.nyu.cs.sh5005;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class Answer {
	static void printAllKey(Node guide,int h,BufferedWriter output) throws IOException{
		   if (h==0) {
			   LeafNode leafPlanet = (LeafNode) guide;
			   output.write(leafPlanet.guide+" "+leafPlanet.value+"\n");
			   return;
		   }
		   else {
			   InternalNode guideNode = (InternalNode) guide;
			   printAllKey(guideNode.child0,h-1,output);
			   printAllKey(guideNode.child1,h-1,output);
			   if (guideNode.child2 != null) {
				   printAllKey(guideNode.child2,h-1,output);
			   }
		   }//else	  
	   }//printAll
    static void printKeyGE(Node guide,String key,int h,BufferedWriter output) throws IOException {
	   if (h == 0) {
		   int compareResult = guide.guide.compareTo(key);
		   if (guide!= null && compareResult>=0) { //in lexicographical order
			   LeafNode leafPlanet = (LeafNode) guide;
			   output.write(leafPlanet.guide+" "+leafPlanet.value+"\n");
		   }
		   return;
	   }
	   InternalNode guideNode = (InternalNode)guide;

	   if(guideNode.child0.guide.compareTo(key)>=0) {
		   printKeyGE(guideNode.child0,key,h-1,output);
		   printAllKey(guideNode.child1,h-1,output);
		   if (guideNode.child2 != null) {
			   printAllKey(guideNode.child2,h-1,output);
		   }
	   }
	   else if (guideNode.child2 == null || guideNode.child1.guide.compareTo(key)>=0){
		   printKeyGE(guideNode.child1,key,h-1,output);
		   if (guideNode.child2 != null) {
			   printAllKey(guideNode.child2,h-1,output);
		   }
	   }
	   else {
		   printKeyGE(guideNode.child2,key,h-1,output);
	   }
    }//printKeyGE
    static void printKeyLE(Node guide,String key,int h,BufferedWriter output) throws IOException {	   
	  
	   if (h == 0) {
		   if (guide!= null && guide.guide.compareTo(key)<=0) { //in lexicographical order
			   LeafNode leafPlanet = (LeafNode) guide;
			   output.write(leafPlanet.guide+" "+leafPlanet.value+"\n");
		   }
		   return;
	   }
	   InternalNode guideNode = (InternalNode)guide;
	   
	   if(key.compareTo(guideNode.child0.guide)<=0) {
		   printKeyLE(guideNode.child0,key,h-1,output);
	   }
	   else if(guideNode.child2 == null || key.compareTo(guideNode.child1.guide) <= 0) {
		   printAllKey(guideNode.child0,h-1,output);
		   printKeyLE(guideNode.child1,key,h-1,output);
	   }
	   else{
		   printAllKey(guideNode.child0,h-1,output);
		   printAllKey(guideNode.child1,h-1,output);
		   printKeyLE(guideNode.child2,key,h-1,output);
		   }
    }//printKeyLE
    static void printKeyRange(Node guide,String lowK,String highK,int h,BufferedWriter output) throws IOException {
	   
	   
	   if (h == 0) {
		   if (guide != null && lowK.compareTo(guide.guide)<=0 && highK.compareTo(guide.guide)>=0) {
			   LeafNode leafPlanet = (LeafNode) guide;
			   output.write(leafPlanet.guide+" "+leafPlanet.value+"\n");
		   }
		   return;
	   }
	   InternalNode guideNode = (InternalNode)guide;
	   
	   if (highK.compareTo(guideNode.child0.guide)<=0) {
		   printKeyRange(guideNode.child0,lowK,highK,h-1,output);
	   }
	   else if (guideNode.child2 == null || highK.compareTo(guideNode.child1.guide)<=0) {
		   if (lowK.compareTo(guideNode.child0.guide)<=0) {
			   printKeyGE(guideNode.child0,lowK,h-1,output);
			   printKeyLE(guideNode.child1,highK,h-1,output);
		   }
		   else {
			   printKeyRange(guideNode.child1,lowK,highK,h-1,output);
		   }
	   }
	   else {
		   if (lowK.compareTo(guideNode.child0.guide)<=0) {
			   printKeyGE(guideNode.child0,lowK,h-1,output);
			   printAllKey(guideNode.child1,h-1,output);
			   printKeyLE(guideNode.child2,highK,h-1,output);
		   }
		   else if (lowK.compareTo(guideNode.child1.guide)<=0){
			   printKeyGE(guideNode.child1,lowK,h-1,output);
			   printKeyLE(guideNode.child2,highK,h-1,output);
		   }
		   else {
			   printKeyRange(guideNode.child2,lowK,highK,h-1,output);
		   }
	   }//else
    }//printKeyRange
   
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
		   for(int i = 0; i< query; i++) {
			   String[] key_planets = input.readLine().split(" ");
			   
			   String key_a = key_planets[0];
			   String key_b = key_planets[1];
			 //compare high key and low key, if low key is lexicographically higher, switch them
			   if(key_a.compareTo(key_b)>0) {
				   String temp =key_b;
				   key_b = key_a;
				   key_a = temp;
			   }
			   printKeyRange(tree.root,key_a,key_b,tree.height,output);
		   }
		   output.flush();
	}

}
