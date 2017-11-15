
public class StringMatch {
	public static boolean kmp(String text, String pattern){
		int tLength = text.length();
		int pLength = pattern.length();
		int[] next = new int[pLength];
		next[0] = -1;
		int index = 0;
		for(int i=1; i<pLength; i++){
			index = next[i-1];
			while(index>=0 && pattern.charAt(index+1)!=pattern.charAt(i)){
				index = next[index];
			}
			if(pattern.charAt(index+1)==pattern.charAt(i)){
				next[i] = index+1;
			}else{
				next[i] = -1;
			}
		}
		//for(int i : next){
			//System.out.print(i+" ");
		//}
		//System.out.println();
		
		int tIndex = 0;
		int pIndex = 0;
		while(tIndex<tLength && pIndex<pLength){
			//System.out.println(tIndex+" "+pIndex);
			if(text.charAt(tIndex)==pattern.charAt(pIndex)){
				tIndex++;
				pIndex++;
				
			}else if(pIndex==0){
				tIndex++;
			}else{
				pIndex = next[pIndex]+1;
			}
		}
		if(pIndex==pLength){
			//System.out.println("matched: "+(tIndex-pIndex));
			return true;
		}else{
			//System.out.println("not matched");
			return false;
		}
	}
	
	public static boolean sunday(String text, String pattern){
		int tLength = text.length();
		int pLength = pattern.length();
		int[] next = new int[256];
		for(int i=0; i<256; i++){
			next[i] = pLength+1;
		}
		for(int i=0; i<pLength; i++){
			next[pattern.charAt(i)] = pLength-i;
		}
		
		int tIndex = 0; 
		int pIndex = 0;
		int step;
		while(tIndex<tLength && pIndex<pLength){
			if(text.charAt(tIndex)==pattern.charAt(pIndex)){
				tIndex++;
				pIndex++;
			}else{
				if(tIndex+pLength-pIndex<tLength){
					step = next[text.charAt(tIndex+pLength-pIndex)];
					tIndex = tIndex-pIndex+step;
					pIndex = 0;
				}else{
					break;
				}
			}
		}
		
		if(pIndex==pLength){
			//System.out.println("matched: "+(tIndex-pIndex));
			return true;
		}else{
			//System.out.println("not matched");
			return false;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String a = "afegergregerabcabagewefabcabaafvvdsbrthroogowpf";
		String b = "abcaba";
		System.out.println(StringMatch.sunday(a, b));

	}

}
