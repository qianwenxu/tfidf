package com.ir.xu1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

class search_word{
	String word;
	int num=1;
}
/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	public static void quickSort(double[] score, int start, int end,int[] sort) {   
	    if (start < end) {   
	        double base = score[start]; // ѡ���Ļ�׼ֵ����һ����ֵ��Ϊ��׼ֵ��   
	        double temp;
	        int temp1; // ��¼��ʱ�м�ֵ   
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
	
	@SuppressWarnings("null")
	@RequestMapping("search")
	public String search(String searchstr,Model model){
		int filenum=0;
		String str = searchstr;
		String srch[]=str.toLowerCase().split("\\W+");
		search_word w[]=new search_word[20];
		int s=0;//��ѯ�ĵ�����
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
            BufferedReader br = new BufferedReader(new FileReader("J:\\doc\\sum.txt"));
            //����һ��BufferedReader������ȡ�ļ�
            int i=0;
            String sm;
			while((sm = br.readLine())!=null){//ʹ��readLine������һ�ζ�һ��
            	sum[i]=Double.parseDouble(sm);
                i++;
                filenum++;
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
		String[] title = new String[filenum];
		try{
            BufferedReader br = new BufferedReader(new FileReader("J:\\doc\\title.txt"));
            //����һ��BufferedReader������ȡ�ļ�
            int i1=0;
            String sm1;
			while((sm1 = br.readLine())!=null){//ʹ��readLine������һ�ζ�һ��
				title[i1]=sm1;
                i1++;
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
		TreeMap<String,invertednode> wordmap=new TreeMap<String,invertednode>();
		try{
            BufferedReader br = new BufferedReader(new FileReader("J:\\doc\\output.txt"));
            //����һ��BufferedReader������ȡ�ļ�
            String line = null;
            while((line = br.readLine())!=null){//ʹ��readLine������һ�ζ�һ��
                String tmp[] = line.split(" ");
                invertednode temp = new invertednode();
                temp.word = tmp[0];
                temp.num = Integer.parseInt(tmp[1]);
                System.out.print(temp.word+":");
                temp.first = new node(Integer.parseInt(tmp[2]));
                temp.first.tf = Double.parseDouble(tmp[3]);
                node first1 = temp.first;
                for(int i=4;i<2*temp.num+1;){
                	first1.next = new node(Integer.parseInt(tmp[i]));
                	first1.next.tf = Double.parseDouble(tmp[i+1]);
                	first1 = first1.next;
                	i+=2;
                	System.out.print(" i:"+i);
                }
                System.out.println();
                wordmap.put(tmp[0], temp);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        double queryweight[]=new double[s];
        double docweight[][]=new double[s][filenum];//s�����ѯ���ʣ��д����ĵ���
        for(int i=0;i<s;i++){
        	invertednode inv=new invertednode();
	        inv = wordmap.get(w[i].word);
	        if(inv==null){
	        	queryweight[i]=0;
	        	for(int j=0;j<filenum;j++){
		        	docweight[i][j]=0;
		        }
	        }else{
	        	System.out.println("inv:"+i);
		        queryweight[i]=Math.log10((float)filenum/(float)inv.num)*(float)(1+Math.log10(w[i].num));
		        System.out.println("queryweight["+i+"]:"+Math.log10((float)filenum/(float)inv.num)+"��(inv.numΪ"+inv.num+")"+(float)(1+Math.log10(w[i].num))+"(w[i]Ϊ"+w[i].num+")");
		        node n=inv.first;
		        for(int j=0;j<filenum;j++){
		        	docweight[i][j]=0;
		        }
		        while(n!=null){
		        	docweight[i][n.DOCID]=(double)n.tf/sum[n.DOCID];
		        	System.out.println("��"+i+"�����ʵ�codweight��"+n.DOCID+"�ĵ�"+docweight[i][n.DOCID]);
		        	n=n.next;
		        }
	        }
        }
        for(int i=0;i<s;i++){
        	System.out.println("queryweight["+i+"]:"+queryweight[i]);
        	for(int j=0;j<filenum;j++){
        		System.out.println("docweight["+i+"]["+j+"]:"+docweight[i][j]);
        	}
        }
        int sort[]=new int[filenum];
        double score[]=new double[filenum];//DOC i�Ļ���
        String titlesort[]=new String[filenum];
        for(int i=0;i<filenum;i++){//����ÿ���ĵ��Ļ���
        	score[i]=0;
        	for(int j=0;j<s;j++){
        		score[i]+=queryweight[j]*docweight[j][i];
        		System.out.println("��"+i+"���ĵ����ּӣ�"+queryweight[j]+"��"+docweight[j][i]);
        	}
        }
        for(int i=0;i<filenum;i++){
        	sort[i] = i;
        	System.out.println("��"+i+"���ĵ����֣�"+score[i]);
        }
        quickSort(score, 0, filenum-1, sort);
        ArrayList<title_and_score> ts =new ArrayList();
        for(int i=0;i<filenum;i++){
        	System.out.println(title[sort[i]]+"���֣�"+score[i]);
        	titlesort[i] = title[sort[i]];
        	title_and_score tas=new title_and_score();
        	tas.score=score[i];
        	tas.title = titlesort[i];
        	ts.add(tas);
        }
        model.addAttribute("ts", ts);
		return "answer";
	}
}
