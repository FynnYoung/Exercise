import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class TreeNode{
	protected int value;
	protected TreeNode leftChild;
	protected TreeNode rightChild;
	
	public TreeNode(){
		
	}
	public TreeNode(int v){
		value = v;
	}
}
public class BinaryTree {
	/**
	 * pre-order and in-order traversal together,
	 * or post-order and in-order traversal together can determine one unique tree,
	 * but pre-order and post-order together cannot.
	 * 
	 * @param pre
	 * @param in
	 * @return
	 */
	public TreeNode orderPreIn(int[] pre, int[] in){
		TreeNode root = new TreeNode();
		root.value = pre[0];
		addChildren(root, pre, in);
		return root;
	}
	public void addChildren(TreeNode parent, int[] pre, int[] in){
		if(pre.length>0 && in.length>0){
			int ri = rootIndex(pre[0], in);
			if(ri>0){
				int[] leftPre = Arrays.copyOfRange(pre, 1, ri+1);
				int[] leftIn = Arrays.copyOfRange(in, 0, ri);
				TreeNode left = new TreeNode();
				left.value = leftPre[0];
				parent.leftChild = left;
				addChildren(left, leftPre, leftIn);
			}
			if(ri<in.length-1){
				int[] rightPre = Arrays.copyOfRange(pre, ri+1, pre.length);
				int[] rightIn = Arrays.copyOfRange(in, ri+1, in.length);
				TreeNode right = new TreeNode();
				right.value = rightPre[0];
				parent.rightChild = right;
				addChildren(right, rightPre, rightIn);
			}
		}
	}
	public int rootIndex(int root, int[] in){
		int index = 0;
		for(index=0; index<in.length; index++){
			if(in[index]==root) break;
		}
		return index;
	}
	
	public boolean hasSubtree(TreeNode tree, TreeNode node){
		boolean result = false;
		if(tree!=null && node!=null){
			if(tree.value==node.value){
				result = doesTree1HaveTree2(tree, node);
			}
			if(!result) result = hasSubtree(tree.leftChild, node);
			if(!result) result = hasSubtree(tree.rightChild, node);
		}
		return result;
	}
	public boolean doesTree1HaveTree2(TreeNode tree, TreeNode node){
		if(node==null) return true;
		if(tree==null) return false;
		if(tree.value!=node.value) return false;
		return doesTree1HaveTree2(tree.leftChild, node.leftChild) && doesTree1HaveTree2(tree.rightChild, node.rightChild);
	}
	
	protected TreeNode lastNodeInList;
	public TreeNode convertToList(TreeNode root){
		lastNodeInList = null;
		convertNode(root);
		TreeNode head = lastNodeInList;
		while(head!=null && head.leftChild!=null){
			head = head.leftChild;
		}
		return head;
	}
	public void convertNode(TreeNode node){
		if(node==null) return;
		TreeNode current = node;
		if(current.leftChild!=null){
			convertNode(current.leftChild);
		}
		current.leftChild = lastNodeInList;
		if(lastNodeInList!=null){
			lastNodeInList.rightChild = current;
		}
		lastNodeInList = current;
		if(current.rightChild!=null){
			convertNode(current.rightChild);
		}
	}
	
	public static void print(TreeNode node){
		Queue<TreeNode> q = new LinkedList<TreeNode>();
		List<TreeNode> list = new ArrayList<TreeNode>();
		q.offer(node);
		while(!q.isEmpty()){
			list.clear();
			while(!q.isEmpty()){
				list.add(q.poll());
			}
			for(int i=0; i<list.size(); i++){
				System.out.print(list.get(i).value+" ");
				if(list.get(i).leftChild!=null){
					q.offer(list.get(i).leftChild);
				}
				if(list.get(i).rightChild!=null){
					q.offer(list.get(i).rightChild);
				}
				
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BinaryTree bt = new BinaryTree();
		//int[] pre = {1,2,4,7,3,5,6,8};
		//int[] in = {4,7,2,1,5,3,8,6};
		//TreeNode root = bt.orderPreIn(pre, in);
		//print(root);
		TreeNode n1 = new TreeNode(8);
		TreeNode n2 = new TreeNode(8);
		TreeNode n3 = new TreeNode(7);
		TreeNode n4 = new TreeNode(9);
		TreeNode n5 = new TreeNode(2);
		TreeNode n6 = new TreeNode(4);
		TreeNode n7 = new TreeNode(7);
		n1.leftChild = n2;
		n1.rightChild = n3;
		n2.leftChild = n4;
		n2.rightChild = n5;
		n5.leftChild = n6;
		n5.rightChild = n7;
		TreeNode n8 = new TreeNode(8);
		TreeNode n9 = new TreeNode(6);
		TreeNode n10 = new TreeNode(2);
		n8.leftChild = n9;
		n8.rightChild = n10;
		System.out.println(bt.hasSubtree(n1, n8));

	}

}
