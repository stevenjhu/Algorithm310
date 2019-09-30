package edu.nyu.cs.sh5005;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Solution {
	static BufferedWriter output;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		output = new BufferedWriter(new OutputStreamWriter(System.out,"ASCII"),4096);
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		TwoThreeTree tree = new TwoThreeTree();
		
		
		int query = Integer.parseInt(input.readLine());
		
		for (int i = 0; i < query; i++) {
			String[] qInfo = input.readLine().split(" ");
			
			if(qInfo.length == 3) {//type 1
				twothree.insert(qInfo[1], Integer.parseInt(qInfo[2]), tree);
			}else if (qInfo.length == 4) {//type 2
				Solution.changeKeyRange(tree.root, qInfo[1], qInfo[2], Integer.parseInt(qInfo[3]), tree.height);
			}else {//type 3
				Solution.searchAndprint(tree.root, qInfo[1],tree.height);
			}
		}
		output.flush();
	}
	static void changeAllKey(Node guide,int delta, int h) throws IOException{
		   if (h==0) {
			   LeafNode leafPlanet = (LeafNode) guide;
			   leafPlanet.value+=delta;
			   return;
		   }
		   else {
			   InternalNode guideNode = (InternalNode) guide;
			   changeAllKey(guideNode.child0,delta,h-1);
			   changeAllKey(guideNode.child1,delta,h-1);
			   if (guideNode.child2 != null) {
				   changeAllKey(guideNode.child2,delta,h-1);
			   }
		   }//else	  
	}//printAll
	static void changeKeyGE(Node guide,String key,int delta,int h) throws IOException {
	   if (h == 0) {
		   int compareResult = guide.guide.compareTo(key);
		   if (guide!= null && compareResult>=0) { //in lexicographical order
			   LeafNode leafPlanet = (LeafNode) guide;
			   leafPlanet.value+=delta;
		   }
		   return;
	   }
	   InternalNode guideNode = (InternalNode)guide;

	   if(guideNode.child0.guide.compareTo(key)>=0) {
		   changeKeyGE(guideNode.child0,key,delta,h-1);
		   changeAllKey(guideNode.child1,delta,h-1);
		   if (guideNode.child2 != null) {
			   changeAllKey(guideNode.child2,delta,h-1);
		   }
	   }
	   else if (guideNode.child2 == null || guideNode.child1.guide.compareTo(key)>=0){
		   changeKeyGE(guideNode.child1,key,delta,h-1);
		   if (guideNode.child2 != null) {
			   changeAllKey(guideNode.child2,delta,h-1);
		   }
	   }
	   else {
		   changeKeyGE(guideNode.child2,key,delta,h-1);
	   }
	}//printKeyGE
	static void changeKeyLE(Node guide,String key,int delta,int h) throws IOException {	   
	  
	   if (h == 0) {
		   if (guide!= null && guide.guide.compareTo(key)<=0) { //in lexicographical order
			   LeafNode leafPlanet = (LeafNode) guide;
			   leafPlanet.value+=delta;
		   }
		   return;
	   }
	   InternalNode guideNode = (InternalNode)guide;
	   
	   if(key.compareTo(guideNode.child0.guide)<=0) {
		   changeKeyLE(guideNode.child0,key,delta,h-1);
	   }
	   else if(guideNode.child2 == null || key.compareTo(guideNode.child1.guide) <= 0) {
		   changeAllKey(guideNode.child0,delta,h-1);
		   changeKeyLE(guideNode.child1,key,delta,h-1);
	   }
	   else{
		   changeAllKey(guideNode.child0,delta,h-1);
		   changeAllKey(guideNode.child1,delta,h-1);
		   changeKeyLE(guideNode.child2,key,delta,h-1);
		   }
	}//printKeyLE
	static void changeKeyRange(Node guide,String lowK,String highK,int delta,int h) throws IOException {
	   
	   
	   if (h == 0) {
		   if (guide != null && lowK.compareTo(guide.guide)<=0 && highK.compareTo(guide.guide)>=0) {
			   LeafNode leafPlanet = (LeafNode) guide;
			   leafPlanet.value+=delta;
		   }
		   return;
	   }
	   InternalNode guideNode = (InternalNode)guide;
	   
	   if (highK.compareTo(guideNode.child0.guide)<=0) {
		   changeKeyRange(guideNode.child0,lowK,highK,delta,h-1);
	   }
	   else if (guideNode.child2 == null || highK.compareTo(guideNode.child1.guide)<=0) {
		   if (lowK.compareTo(guideNode.child0.guide)<=0) {
			   changeKeyGE(guideNode.child0,lowK,delta,h-1);
			   changeKeyLE(guideNode.child1,highK,delta,h-1);
		   }
		   else {
			   changeKeyRange(guideNode.child1,lowK,highK,delta,h-1);
		   }
	   }
	   else {
		   if (lowK.compareTo(guideNode.child0.guide)<=0) {
			   changeKeyGE(guideNode.child0,lowK,delta,h-1);
			   changeAllKey(guideNode.child1,delta,h-1);
			   changeKeyLE(guideNode.child2,highK,delta,h-1);
		   }
		   else if (lowK.compareTo(guideNode.child1.guide)<=0){
			   changeKeyGE(guideNode.child1,lowK,delta,h-1);
			   changeKeyLE(guideNode.child2,highK,delta,h-1);
		   }
		   else {
			   changeKeyRange(guideNode.child2,lowK,highK,delta,h-1);
		   }
	   }//else
	}//printKeyRange
	static void searchAndprint(Node guide,String key,int h) throws IOException {
		if(h==0) {
			if(key.compareTo(guide.guide)==0) {
				output.write(guide.value+"\n");
				return;
			}else {
				output.write("-1\n");
				return;
			}
		}
		InternalNode n = (InternalNode) guide;
		if(key.compareTo(n.child0.guide)<=0) {
			searchAndprint(n.child0,key,h-1);
		}else if(n.child2 == null || key.compareTo(n.child1.guide)<=0) {
			searchAndprint(n.child1,key,h-1);
		}else {
			searchAndprint(n.child2,key,h-1);
		}
	}
}
