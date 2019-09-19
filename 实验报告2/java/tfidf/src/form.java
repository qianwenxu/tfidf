import java.io.*;
import java.util.*;

class myMergeSort {
    static int number=0;
    static void MergeSort(docId[] a,int k) {
        // TODO Auto-generated method stub
        System.out.println("开始排序");
        Sort(a, 0, k - 1);
    }
    private static void Sort(docId[] a, int left, int right) {
        if(left>=right)
            return;
    
        int mid = (left + right) / 2;
        //二路归并排序里面有两个Sort，多路归并排序里面写多个Sort就可以了
        Sort(a, left, mid);
        Sort(a, mid + 1, right);
        merge(a, left, mid, right);

    }
    private static void merge(docId[] a, int left, int mid, int right) {
    
        docId[] tmp = new docId[a.length];
        int r1 = mid + 1;
        int tIndex = left;
        int cIndex=left;
        // 逐个归并
        while(left <=mid && r1 <= right) {
            if (a[left].word.compareTo(a[r1].word)<0||(a[left].word.compareTo(a[r1].word)==0&&a[left].docID<a[r1].docID))
                tmp[tIndex++] = a[left++];
            else
                tmp[tIndex++] = a[r1++];
        }
        // 将左边剩余的归并
        while (left <=mid) {
            tmp[tIndex++] = a[left++];
        }
        // 将右边剩余的归并
        while ( r1 <= right ) {
            tmp[tIndex++] = a[r1++];
        }
            
        //System.out.println("第"+(++number)+"趟排序:\t");
        // TODO Auto-generated method stub
        //从临时数组拷贝到原数组
        while(cIndex<=right){
            a[cIndex]=tmp[cIndex];
            //输出中间归并排序结果
            //System.out.print("<"+a[cIndex].word+","+a[cIndex].docID+">"+"\t");
            cIndex++;
        }
        //System.out.println();
    }
}

class node{
	int DOCID;
	double tf=1;
	node next=null;
	node(int i){
		DOCID = i;
	}
}

class invertednode{
	public String word = null;
	public int num = 0;
	public node first = null;
}

class docId{
	String word;
	int docID;
	docId(String word1,int docID1){
		word = word1;
		docID = docID1;
	}
	void show(){
		//System.out.println("<"+word+","+docID+">");
	}
}
public class form {
	public static invertednode[] splitstring(){
        // get file list where the path has   
        File file = new File("doc\\input");   
        // get the folder list   
        File[] array = file.listFiles();   
        String title[] = new String[array.length];
        String article[] = new String[array.length];
        for(int i=0;i<array.length;i++){   
            if(array[i].isFile()){   
                // only take file name
                System.out.println("^^^^^" + array[i].getName());
                title[i] = array[i].getName().substring(0, array[i].getName().indexOf("."));
                StringBuilder result = new StringBuilder();
                try{
                    BufferedReader br = new BufferedReader(new FileReader(file+"\\"+array[i].getName()));//构造一个BufferedReader类来读取文件
                    String s = br.readLine();
                    result.append(s);
                    while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                        result.append(System.lineSeparator()+s);
                    }
                    br.close();    
                }catch(Exception e){
                    e.printStackTrace();
                }
                //System.out.println(result.toString());
                article[i] = result.toString();
            }
        }
	    docId doc[]=null;
	    doc = new docId[1000000];
	    int k=0;
		for(int i = 0;i < array.length;i++){
		    String temp[] = article[i].split("\\W+");
		    //System.out.println(temp[2]);
		    //System.out.println(java.util.Arrays.toString(temp));
		    for(int j=0;j<temp.length;j++){
		    	if(!temp[j].equals("")){
		    		doc[k] = new docId(temp[j].toLowerCase(),i);
		    	    //doc[k].show();
		    	    k++;
		    	}
		    }
	    };
	    //System.out.println("共有k:"+(k-1));
	    myMergeSort ms = new myMergeSort();
	    ms.MergeSort(doc,k);
	    int s = 0;
	    invertednode inNode[] = new invertednode[100000];
	    node tmp = null;
	    node tmp1 = null;
	    for(int i=0;i<k;i++){
	    	System.out.print("<"+doc[i].word+","+doc[i].docID+">"+"    ");
	    	//node tmp = new node(-1);
	    	//node tmp1 = new node(-1);
	    	invertednode innode1 = new invertednode();
	    	if(i == 0){
	    		innode1.word = doc[0].word;
	    		innode1.num++;
	    		inNode[0] = innode1;
	    		tmp = new node(doc[0].docID);
	    		//tmp.next = null;
	    		inNode[0].first = tmp;
	    		int j;
	    		for(j=0;doc[i].word.equals(doc[i+j].word)&&doc[i].docID==doc[i+j].docID&&i+j<=k-1;){j++;}
	    		tmp.tf=j;
	    		s++;
	    	}else{
	    		if((doc[i].word.equals(doc[i-1].word))){
	    			if(doc[i].docID != doc[i-1].docID){
	    				inNode[s-1].num++;
	    				tmp1=new node(doc[i].docID);
		    			int j;
			    		for(j=0;i+j<=k-1;){
			    			if(doc[i].word.equals(doc[i+j].word)&&doc[i].docID==doc[i+j].docID){
			    				j++;
			    			}else{
			    				break;
			    			}
			    		}
	    	    		tmp1.tf=j;
	    				tmp.next = tmp1;
	    	    		tmp = tmp.next;
	    			}
	    		}else{
	    			invertednode innode2 = new invertednode();
	    			innode2.word = doc[i].word;
	    			innode2.num++;
	    			inNode[s] = innode2;
	    			tmp = new node(doc[i].docID);
	    			int j;
		    		for(j=0;i+j<=k-1;){
		    			if(doc[i].word.equals(doc[i+j].word)&&doc[i].docID==doc[i+j].docID){
		    				j++;
		    			}else{
		    				break;
		    			}
		    		}
		    		tmp.tf=j;
		    		tmp.next = null;
		    		inNode[s].first = tmp;
		    		s++;
	    		}
	    	}
	    }
	    double sum[]=new double[array.length];//用于余弦归一化的分母
	    File writename = new File("doc\\output.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件  
        try {
			writename.createNewFile();// 创建新文件  
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));  
		    for(int i=0;i<s;i++){
		    	//System.out.print(inNode[i].word+"|"+inNode[i].num+"     ");
		    	out.write(inNode[i].word+" "+inNode[i].num+" "); // \r\n即为换行  
		    	out.flush(); // 把缓存区内容压入文件  
		    	node n=inNode[i].first;
		    	System.out.println();
		    	while(n!=null){
		    		//System.out.print(n.DOCID+"   ");
		    		System.out.println("n.tf->1+Math.log10(n.tf)"+n.tf);
		    		n.tf=1+Math.log10(n.tf);
		    		out.write(n.DOCID+" "+n.tf+" ");
		    		sum[n.DOCID]+=n.tf*n.tf;
		    		System.out.println(n.DOCID+"号"+n.tf+"的平方");
		    		out.flush(); // 把缓存区内容压入文件  
		    		n=n.next;
		    	}
		    	//System.out.println();
		        if(i!=s-1) {out.write("\r\n");}
	    		out.flush(); // 把缓存区内容压入文件 
		    }
		    out.close(); // 最后记得关闭文件	
		    for(int i=0;i<array.length;i++){
		    	sum[i]=Math.sqrt(sum[i]);
		    }
		    File writename1 = new File("doc\\title.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件  
			writename1.createNewFile();// 创建新文件  
			BufferedWriter out1 = new BufferedWriter(new FileWriter(writename1));  
			for(int i=0;i<array.length-1;i++){
		    	out1.write(title[i]+"\r\n"); // \r\n即为换行  
		    	out1.flush(); // 把缓存区内容压入文件  
			}
			out1.write(title[array.length-1]); // \r\n即为换行  
	    	out1.flush(); // 把缓存区内容压入文件  
			out1.close(); // 最后记得关闭文件
			//存储sum[i]
			File writename2 = new File("doc\\sum.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件  
			writename2.createNewFile();// 创建新文件  
			BufferedWriter out2 = new BufferedWriter(new FileWriter(writename2));  
			for(int i=0;i<array.length-1;i++){
		    	out2.write(sum[i]+"\r\n"); // \r\n即为换行  
		    	out2.flush(); // 把缓存区内容压入文件  
			}
			out2.write(sum[array.length-1]+"");
	    	out2.flush(); // 把缓存区内容压入文件  
			out2.close(); // 最后记得关闭文件
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("结束");
	    return inNode;
	}
	public static void main(String[] args){
		form p = new form();
		//Scanner in = new Scanner(System.in);
		//String str = in.nextLine().toLowerCase();
		//String word1=in.nextLine().toLowerCase();
		//Scanner in = new Scanner(System.in);
		//String word2=in.nextLine().toLowerCase();
		p.splitstring();
		//p.splitorandnot(str);
		//System.out.println(java.util.Arrays.toString(result));
		//p.search_doc();
	}
}