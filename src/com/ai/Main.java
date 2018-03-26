package com.ai;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.math.*;
import java.awt.event.ActionListener;


public abstract class Main {

    static int nodes = 8;
    static int cost = 1;
    static int gen = 100;
    static int size = 20;
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                makeWindow();
            }
        });
    }
    private static void makeWindow() {
        //Create and set up the window.
        JFrame f = new JFrame("8 Puzzle");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);
        f.setLayout(new BorderLayout());

        //matrix a=goal b=start
        String[][] a = {{"0","1","2"},{"3","4","5"},{"6","7","8"}};
        String[][] b = {{"7","2","4"},{"5","0","6"},{"8","3","1"}};
        int[][] ina = {{0,0,0},{0,0,0},{0,0,0}};
        int[][] inb = {{0,0,0},{0,0,0},{0,0,0}};
        String[] col={" "," "," "};
        for(int i=0; i<a.length; i++){
            for (int j=0; j<a[i].length; j++){
                ina[i][j]=Integer.parseInt(a[i][j]);
                inb[i][j]=Integer.parseInt(b[i][j]);
            }
        }
        JTable tbla = new JTable(a, col);
        JTable tblb = new JTable(b, col);
        for(int i=0; i<a.length; i++){
            for (int j=0; j<a[i].length; j++){
                a[i][j]=Integer.toString(ina[i][j]);
                b[i][j]=Integer.toString(inb[i][j]);
            }
        }

        f.add(tbla.getTableHeader(), BorderLayout.PAGE_START);
        f.add(tbla,BorderLayout.WEST);
        f.add(tblb.getTableHeader(),BorderLayout.PAGE_START);
        f.add(tblb,BorderLayout.EAST);


        //button
        JButton button = new JButton("Start");
        f.add(button,BorderLayout.SOUTH);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                int[] answer=geneticAlgorithm(ina, inb);
                int[][] tbl3=array23x3Mat(answer);
                for(int i=0; i<a.length; i++){
                    for (int j=0; j<a[i].length; j++){
                        a[i][j]=Integer.toString(ina[i][j]);
                        b[i][j]=Integer.toString(tbl3[i][j]);
                    }
                }
                tblb.invalidate();
            }
        });

        //Display the window.
        f.pack();
        f.setVisible(true);
    }
    
    /*Here we're going to do: 
     * Make a population{using crossover then mutation)
     * Create successor populations by mutating and crossing
     * Test each member of the chromosome population and 
     * return the FITTEST
     * fitness test:
     * 
     * 
     * 
     * */
    
    public static int[] geneticAlgorithm(int [][] a, int[][] b){
        if(b!=a) {
            int[] arra= mat2Array(a);
            int[] arrb=mat2Array(b);
            int index = 0;
            int[] result = new int[9];
            List<int[]> popul1=makePopul(arra, arrb);
            List<int[]> popul2=makePopul(arra, arrb);
            List<int[]> popul3=new ArrayList<int[]>();
            //test popul1 and 2 for fitness and output the fittest one to popul5
            //then test fitness between popul5
            index=0;
            while(index<popul1.size()) {
            		if(fitnessFunction(popul1.get(index))>fitnessFunction(popul2.get(index))){
            			popul3.add(popul1.get(index));
            		}
            		else {
            			popul3.add(popul2.get(index));
            		}
            		index++;
            }
            //crossover, then mutate
            index=0;
            List<int[]> temp1 = new ArrayList();
            List<int[]> temp2 = new ArrayList();
            while(index<popul3.size()/2) {
            	temp1.add(popul3.get(index));
            	temp2.add(popul3.get((index+popul3.size()/2)));
            	index++;
            }
            index=0;
            while(index<popul3.size()/2) {
            		List<int[]> temp= crossover(temp1.get(index), temp2.get(index));
            		temp1.remove(index);
            		temp2.remove(index);
            		temp1.add(index, temp.get(0));
            		temp2.add(index, temp.get(1));
            		index++;
            }
            index=0;
            while(index<popul3.size()) {
            		int[] tmp=mutate(popul3.get(index));
            		popul3.remove(index);
            		popul3.add(index, tmp);
            		index++;
            		
            }
            index=0;
            while(index<temp1.size()) {
            	//popul3.remove(index);
            	//popul3.remove(index+temp1.size());
            	popul3.add(index, temp1.get(index));
            	popul3.add(index+temp1.size(), temp2.get(index));
            	index++;
            }
            //popul3=temp1+temp2;
            int min=fitnessFunction(popul3.get(0));
            int index2=0;
            for(int i=1; i<popul3.size(); i++) {
            		if(fitnessFunction(popul3.get(index))<min) {
            			min=fitnessFunction(popul3.get(index));
            			index2=i;
            		}
            }
            result=popul3.get(index2);
            for(int i=0; i<result.length; i++) {
            	System.out.print(result[i]+", ");
            }
            return result;
        }
        else {
        	return mat2Array(b);
        }
    }
    
    
    /*
     * utility functions, i probably don't need all of them, 
     * they were made because this was stuff i might need and
     * didnt want to type out more than once
    */
    public static int[][] cloneMat(int[][] src) {
        int length = src.length;
        int[][] target = new int[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }
    public static int[] mat2Array(int[][] src) {
        int length = src.length;
        int[] target = new int[length*src[0].length];
        for (int i = 0; i < length; i++) {
        	for(int j = 0; j < src[i].length; j++) {
        		target[j+i*3]=src[i][j];
        	}
        }
        return target;
    }
    public static int[][] array23x3Mat(int[] src){
    	int target[][] = new int[3][3];
    	for (int i = 0; i < 3; i++) {
        	for(int j = 0; j < 3; j++) {
        		target[i][j]=src[j+i*3];
        	}
        }
    	return target;
    	
    }
    public static int manhattanMat(int[][] a, int[][] b){
    	int mandis = 0;
        int index = 1;

        while (index < nodes){
            int inib = 0;
            int injb = 0;
            int inia = 0;
            int inja = 0;
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b[i].length; j++) {
                    if (b[i][j] == index) {
                        inib = i;
                        injb = j;
                    }
                }
            }
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[i].length; j++) {
                    if (a[i][j] == index) {
                        inia = i;
                        inja = j;
                    }
                }
            }
            mandis+=(Math.abs(inib-inia) + Math.abs(injb-inja));
            index++;
        }
        return mandis;
    }
    public static int manhattanArray(int[] a, int[] b) {
    	int mandis = 0;
    	int[][] mata = array23x3Mat(a);
    	int[][] matb = array23x3Mat(b);
    	mandis = manhattanMat(mata,matb);
    	
    	return mandis;
    }
    public static int getMisplaced(int[]a, int[] b) {
    	int count = 0;
    	for(int i=0; i<a.length; i++) {
    		if (a[i]!=b[i]) {
    			count++;
    		}
    	}
    	return count;
    }
    public static int fitnessFunction(int[] chrom) {
    	int fit = 0;
    	for(int i=0; i<chrom.length; i++) {
    		fit+=chrom[i]*i;
    	}
    	return fit;
    }
    public static double prob(int[] chrom, int index) {
    	return (chrom[index]*index)/fitnessFunction(chrom);
    }
    public static int[] mutate(int[] src) {
    		Random rand = new Random();
    		double pm = 0.011;
    		int[] target = src.clone();
    		if(Math.random()<pm) {
    			int p1=rand.nextInt(src.length);
    			int p2=rand.nextInt(src.length);
    			int temp=target[p1];
    			target[p1]=target[p2];
    			target[p2]=temp;
    		}
    		return target;
    }
    public static List<int[]> crossover(int[] srcA, int[] srcB) {
    	List<int[]> out = new ArrayList<int[]>();
    	int[] child1=srcA.clone();
    	int[] child2=srcB.clone();
    	double pc = 0.7;
    	Random rand = new Random();
    	int point = 0;
    	if(Math.random()>pc) {
    		point=rand.nextInt(srcA.length);
    		//int[] temp = new int[srcA.length-point];
    		for(int i=point; i<srcA.length; i++) {
    			int temp=child1[i];
    			child1[i]=child2[i];
    			child2[i]=temp;
    		}
    		
    	}
    	out.add(child1);
    	out.add(child2);
    	return out;
    }
    public static boolean isFit(int[] chrom, int[] a) {
    	if(fitnessFunction(chrom)==fitnessFunction(a)) {
    		return true;
    	}
    	else return false;
    }
    public static List<int[]> makePopul(int[] a, int[]b){
    	List<int[]> popul1 = new ArrayList<int[]>();
    	List<int[]> popul2 = new ArrayList<int[]>();
    	int index =0;
    	 //crossover
        popul1.add(a);
       	popul1.add(b);
       	for(int i=0; i<(size-2)/2; i++) {
       		List<int[]> crossout =crossover(a, b);
        	popul1.add(crossout.get(0));
        	popul1.add(crossout.get(1));
        }
        	
        index=0;
        //mutate
        while(index<popul1.size()) {
        	popul2.add(mutate(popul1.get(index)));
        	index++;
        }
        return popul2;
       
    }
}

    
