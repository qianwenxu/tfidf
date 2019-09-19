import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

class search_word{
	String word;
	int num=1;
}
public class Search {
	public static void quickSort(double[] score, int start, int end,int[] sort) {   
	    if (start < end) {   
	        double base = score[start]; // 选定的基准值（第一个数值作为基准值）   
	        double temp;
	        int temp1; // 记录临时中间值   
	        int i = start, j = end;   
	        do {   
	            while ((score[i] > base) && (i < end))   
	                i++;   
	            while ((score[j] < base) && (j > start))   
	                j--;   
	            if (i <= j) {   
	                temp = score[i];   
	                temp1=sort[i];
	                score[i] = score[j];
	                sort[i] = sort[j];
	                score[j] = temp;
	                sort[j] = temp1;
	                i++;   
	                j--;   
	            }   
	        } while (i <= j);   
	        if (start < j)   
	            quickSort(score, start, j,sort);   
	        if (end > i)   
	            quickSort(score, i, end,sort);   
	    }   
	} 
	
	public static void main(String args[]){
		while(true){
			int filenum=0;
			Scanner in = new Scanner(System.in);
			String str = in.nextLine().toLowerCase();
			String srch[]=str.toLowerCase().split("\\W+");
			search_word w[]=new search_word[20];
			int s=0;//查询的单词数
			for(int i=0;i<srch.length;i++){
				if(i==0){
					w[s]=new search_word();
					w[s].word=srch[i];
				    s++;
				}else{
					boolean exist=false;
					for(int j=0;j<s;j++){
						if(w[j].word.equals(srch[i])){
							w[j].num++;
							exist=true;
						}
					}
					if(!exist){
						w[s]=new search_word();
						w[s].word=srch[i];
						s++;
					}
				}
			}
			double[] sum = new double[20];
			try{
	            BufferedReader br = new BufferedReader(new FileReader("doc\\sum.txt"));
	            //构造一个BufferedReader类来读取文件
	            int i=0;
	            String sm;
				while((sm = br.readLine())!=null){//使用readLine方法，一次读一行
	            	sum[i]=Double.parseDouble(sm);
	                i++;
	                filenum++;
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        String[] title = new String[20];
			try{
	            BufferedReader br = new BufferedReader(new FileReader("doc\\title.txt"));
	            //构造一个BufferedReader类来读取文件
	            int i1=0;
	            String sm1;
				while((sm1 = br.readLine())!=null){//使用readLine方法，一次读一行
					title[i1]=sm1;
	                i1++;
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
			TreeMap<String,invertednode> wordmap=new TreeMap<String,invertednode>();
			try{
	            BufferedReader br = new BufferedReader(new FileReader("doc\\output.txt"));
	            //构造一个BufferedReader类来读取文件
	            String line = null;
	            while((line = br.readLine())!=null){//使用readLine方法，一次读一行
	                String tmp[] = line.split(" ");
	                invertednode temp = new invertednode();
	                temp.word = tmp[0];
	                temp.num = Integer.parseInt(tmp[1]);
	                //System.out.print(temp.word+":");
	                temp.first = new node(Integer.parseInt(tmp[2]));
	                temp.first.tf = Double.parseDouble(tmp[3]);
	                node first1 = temp.first;
	                for(int i=4;i<2*temp.num+1;){
	                	first1.next = new node(Integer.parseInt(tmp[i]));
	                	first1.next.tf = Double.parseDouble(tmp[i+1]);
	                	first1 = first1.next;
	                	i+=2;
	                	//System.out.print(" i:"+i);
	                }
	                //System.out.println();
	                wordmap.put(tmp[0], temp);
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        double queryweight[]=new double[s];
	        double docweight[][]=new double[s][filenum];//s代表查询单词，列代表文档号
	        for(int i=0;i<s;i++){
	        	invertednode inv=new invertednode();
		        inv = wordmap.get(w[i].word);
		        if(inv==null){
		        	queryweight[i]=0;
		        	for(int j=0;j<filenum;j++){
			        	docweight[i][j]=0;
			        }
		        }else{
		        	//System.out.println("inv:"+i);
			        queryweight[i]=Math.log10((float)filenum/(float)inv.num)*(float)(1+Math.log10(w[i].num));
			        //System.out.println("queryweight["+i+"]:"+Math.log10((float)filenum/(float)inv.num)+"乘(inv.num为"+inv.num+")"+(float)(1+Math.log10(w[i].num))+"(w[i]为"+w[i].num+")");
			        node n=inv.first;
			        for(int j=0;j<filenum;j++){
			        	docweight[i][j]=0;
			        }
			        while(n!=null){
			        	docweight[i][n.DOCID]=(double)n.tf/sum[n.DOCID];
			        	//System.out.println("第"+i+"个单词的codweight在"+n.DOCID+"文档"+docweight[i][n.DOCID]);
			        	n=n.next;
			        }
		        }
	        }
	        for(int i=0;i<s;i++){
	        	//System.out.println("queryweight["+i+"]:"+queryweight[i]);
	        	for(int j=0;j<filenum;j++){
	        		//System.out.println("docweight["+i+"]["+j+"]:"+docweight[i][j]);
	        	}
	        }
	        int sort[]=new int[filenum];
	        double score[]=new double[filenum];//DOC i的积分
	        for(int i=0;i<filenum;i++){//计算每个文档的积分
	        	score[i]=0;
	        	for(int j=0;j<s;j++){
	        		score[i]+=queryweight[j]*docweight[j][i];
	        		//System.out.println("第"+i+"个文档积分加："+queryweight[j]+"乘"+docweight[j][i]);
	        	}
	        }
	        for(int i=0;i<filenum;i++){
	        	sort[i] = i;
	        	//System.out.println("第"+i+"个文档积分："+score[i]);
	        }
	        quickSort(score, 0, filenum-1, sort);
	        for(int i=0;i<5;i++){
	        	System.out.println(title[sort[i]]+"积分："+score[i]);
	        }
		}
	}
}
