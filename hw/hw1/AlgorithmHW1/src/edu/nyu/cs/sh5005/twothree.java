package edu.nyu.cs.sh5005;

import java.io.*;

class Node {
   String guide;
   // guide points to max key in subtree rooted at node
}

class InternalNode extends Node {
   Node child0,child1,child2;
   // child0 and child1 are always non-null
   // child2 is null iff node has only 2 children
}

class LeafNode extends Node {
   // guide points to the key

   int value;
}

class TwoThreeTree {
   Node root;
   int height;

   TwoThreeTree() {
      root = null;
      height = -1;
   }
}

class WorkSpace {
// this class is used to hold return values for the recursive doInsert
// routine (see below)

   Node newNode;
   int offset;
   boolean guideChanged;
   Node[] scratch;
}
class Keys{
	String[] key = new String[1];
}
class twothree {

   public static void main(String[] args) throws UnsupportedEncodingException,IOException {
	   BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out,"ASCII"),4096);
	   BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	   TwoThreeTree tree = new TwoThreeTree();
	   
	   int num_planet = Integer.parseInt(input.readLine());
	   for(int i = 0; i< num_planet; i++) {
		   String[] list = input.readLine().split(" ");
		   String key = list[0];
		   int value = Integer.parseInt(list[1]);
		   insert(key,value,tree);
	   }
	   
	   int query = Integer.parseInt(input.readLine());
	   Keys[] chain = new Keys[query];
	   
	   
	   for(int i = 0; i< query; i++) {
		   String[] key_planets = input.readLine().split(" ");
		   chain[i] = new Keys();
		   chain[i].key = key_planets;
	   }
	   for(Keys key: chain) {
		   printKeyRange((InternalNode)tree.root,key.key[0],key.key[1],tree.height,output);
	   }
	   
	   
	   output.flush();
   }
   
   static void printAllKey(Node guide,int h,BufferedWriter output) throws IOException{
	   if (h==0) {
		   output.write(((LeafNode)guide).guide+"\n");
	   }
	   else {
		   printAllKey(((InternalNode)guide).child0,h-1,output);
		   printAllKey(((InternalNode)guide).child1,h-1,output);
		   if (((InternalNode)guide).child2 != null) {
			   printAllKey(((InternalNode)guide).child2,h-1,output);
		   }
	   }//else	  
   }//printAll
   static void printKeyGE(Node guide,String key,int h,BufferedWriter output) throws IOException {
	   Node child0 = new Node(),child1 = new Node(),child2 = new Node();
	   
	   if(!(guide instanceof LeafNode)&&guide == null) {
		   child0 = (((InternalNode)guide).child0);
		   child1 = (((InternalNode)guide).child1);
		   child2 = (((InternalNode)guide).child2);
	   }
	   if (h == 0) {
		   if (guide.guide.compareTo(key)>=0) { //in lexicographical order
			   output.write(guide.guide+"\n");
		   }
		   printKeyGE(child0,key,h-1,output);
		   
		   printAllKey(child0,h-1,output);
		   printKeyGE(child1,key,h-1,output);
	   }
	   else {
		   printAllKey(child0,h-1,output);
		   printAllKey(child1,h-1,output);
		   if (child2 != null)
			   printKeyGE(child2,key,h-1,output);
	   }
   }//printKeyGE
   static void printKeyLE(Node guide,String key,int h,BufferedWriter output) throws IOException {
	   Node child0 = new Node(),child1 = new Node(),child2 = new Node();
	   
	   if(!(guide instanceof LeafNode)) {
		   child0 = (((InternalNode)guide).child0);
		   child1 = (((InternalNode)guide).child1);
		   child2 = (((InternalNode)guide).child2);
	   }
	   if (h == 0) {
		   if (guide.guide.compareTo(key)<=0) { //in lexicographical order
			   output.write(guide.guide+"\n");
		   }
		   
		   printKeyLE(child0,key,h-1,output);
		   
		   printAllKey(child0,h-1,output);
		   printKeyLE(child1,key,h-1,output);
	   }
	   else {
		   printAllKey(child0,h-1,output);
		   printAllKey(child1,h-1,output);
		   if (child2 != null)
			   printKeyLE(child2,key,h-1,output);
	   }
   }//printKeyLE
   static void printKeyRange(Node guide,String lowK,String highK,int h,BufferedWriter output) throws IOException {
	   InternalNode child0,child1,child2;
	   
	   
		   child0 = ((InternalNode)guide).child0;
		   child1 = (((InternalNode)guide).child1);
		   child2 = (((InternalNode)guide).child2);
	   
	   
	   if (h == 0) {
		   if (lowK.compareTo(guide.guide)<=0 && highK.compareTo(guide.guide)>=0) {
			   output.write(guide.guide+"\n");
		   }
	   }
	   else if (highK.compareTo(child0.guide)<=0) {
		   printKeyRange(child0,lowK,highK,h-1,output);
	   }
	   else if (child2 == null || highK.compareTo(child1.guide)<=0) {
		   if (lowK.compareTo(child0.guide)<=0) {
			   printKeyGE(child0,lowK,h-1,output);
			   printKeyLE(child1,highK,h-1,output);
		   }
		   else {
			   printKeyRange(child1,lowK,highK,h-1,output);
		   }
	   }
	   else {
		   if (lowK.compareTo(child0.guide)<=0) {
			   printKeyGE(child0,lowK,h-1,output);
			   printAllKey(child1,h-1,output);
			   printKeyLE(child2,highK,h-1,output);
		   }
		   else if (lowK.compareTo(child1.guide)<=0){
			   printKeyGE(child1,lowK,h-1,output);
			   printKeyLE(child2,highK,h-1,output);
		   }
		   else {
			   printKeyRange(child2,lowK,highK,h-1,output);
		   }
	   }
   }
   static void insert(String key,int value,TwoThreeTree tree) {
   // insert a key value pair into tree (overwrite existsing value
   // if key is already present)

      int h = tree.height;

      if (h == -1) {
          LeafNode newLeaf = new LeafNode();
          newLeaf.guide = key;
          newLeaf.value = value;
          tree.root = newLeaf; 
          tree.height = 0;
      }
      else {
         WorkSpace ws = doInsert(key,value,tree.root,h);

         if (ws != null && ws.newNode != null) {
         // create a new root

            InternalNode newRoot = new InternalNode();
            if (ws.offset == 0) {
               newRoot.child0 = ws.newNode; 
               newRoot.child1 = tree.root;
            }
            else {
               newRoot.child0 = tree.root; 
               newRoot.child1 = ws.newNode;
            }
            resetGuide(newRoot);
            tree.root = newRoot;
            tree.height = h+1;
         }
      }
   }

   static WorkSpace doInsert(String key,int value,Node p,int h) {
   // auxiliary recursive routine for insert

      if (h == 0) {
         // we're at the leaf level,so compare and 
         // either update value or insert new leaf

         LeafNode leaf = (LeafNode) p; //downcast
         int cmp = key.compareTo(leaf.guide);

         if (cmp == 0) {
            leaf.value = value; 
            return null;
         }

         // create new leaf node and insert into tree
         LeafNode newLeaf = new LeafNode();
         newLeaf.guide = key; 
         newLeaf.value = value;

         int offset = (cmp < 0) ? 0 : 1;
         // offset == 0 => newLeaf inserted as left sibling
         // offset == 1 => newLeaf inserted as right sibling

         WorkSpace ws = new WorkSpace();
         ws.newNode = newLeaf;
         ws.offset = offset;
         ws.scratch = new Node[4];

         return ws;
      }
      else {
         InternalNode q = (InternalNode) p; // downcast
         int pos;
         WorkSpace ws;

         if (key.compareTo(q.child0.guide) <= 0) {
            pos = 0; 
            ws = doInsert(key,value,q.child0,h-1);
         }
         else if (key.compareTo(q.child1.guide) <= 0 || q.child2 == null) {
            pos = 1;
            ws = doInsert(key,value,q.child1,h-1);
         }
         else {
            pos = 2; 
            ws = doInsert(key,value,q.child2,h-1);
         }

         if (ws != null) {
            if (ws.newNode != null) {
               // make ws.newNode child # pos + ws.offset of q

               int sz = copyOutChildren(q,ws.scratch);
               insertNode(ws.scratch,ws.newNode,sz,pos + ws.offset);
               if (sz == 2) {
                  ws.newNode = null;
                  ws.guideChanged = resetChildren(q,ws.scratch,0,3);
               }
               else {
                  ws.newNode = new InternalNode();
                  ws.offset = 1;
                  resetChildren(q,ws.scratch,0,2);
                  resetChildren((InternalNode) ws.newNode,ws.scratch,2,2);
               }
            }
            else if (ws.guideChanged) {
               ws.guideChanged = resetGuide(q);
            }
         }

         return ws;
      }
   }

   static int copyOutChildren(InternalNode q,Node[] x) {
   // copy children of q into x,and return # of children

      int sz = 2;
      x[0] = q.child0; x[1] = q.child1;
      if (q.child2 != null) {
         x[2] = q.child2; 
         sz = 3;
      }
      return sz;
   }

   static void insertNode(Node[] x,Node p,int sz,int pos) {
   // insert p in x[0..sz) at position pos,
   // moving existing extries to the right

      for (int i = sz; i > pos; i--)
         x[i] = x[i-1];

      x[pos] = p;
   }

   static boolean resetGuide(InternalNode q) {
   // reset q.guide,and return true if it changes.

      String oldGuide = q.guide;
      if (q.child2 != null)
         q.guide = q.child2.guide;
      else
         q.guide = q.child1.guide;

      return q.guide != oldGuide;
   }

   static boolean resetChildren(InternalNode q,Node[] x,int pos,int sz) {
   // reset q's children to x[pos..pos+sz),where sz is 2 or 3.
   // also resets guide,and returns the result of that

      q.child0 = x[pos]; 
      q.child1 = x[pos+1];

      if (sz == 3) 
         q.child2 = x[pos+2];
      else
         q.child2 = null;

      return resetGuide(q);
   }
}

