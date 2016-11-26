/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task01;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alexis
 */
public class Task01 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        if(args.length != 2)
        {
            System.out.println("Error usage : java input_file output_file");
            System.exit(-1);
        }
        int documents_rel_five = 0;
        int documents_rel_twenty = 0;
        int documents_rel_hundred = 0;
        int query_id = 0;
        int previous_query = 0;
        int relevant_number = 0;
        int tot_documents = 0;
        DecimalFormat df = new DecimalFormat("#.####");
        
        LinkedHashMap<Integer,Integer> relnumbermap = new LinkedHashMap<>();
        Map<Integer,List<String>> map = new HashMap<>();
        Map<Integer,Integer> relret = new HashMap<>();
        Map<Integer,Integer> ret = new HashMap<>();
        Map<Integer,Integer> precision_five = new HashMap<>();
        Map<Integer,Integer> precision_twenty = new HashMap<>();
        Map<Integer,Integer> precision_hundred = new HashMap<>();
        // open and read the file
        try (BufferedReader reader = new BufferedReader(new FileReader(args[1]))) {
            String line;
            // read each document (line)
            while ((line = reader.readLine()) != null)
            {
                
                String[] words = line.split(" ");
                query_id = Integer.valueOf(words[0]);
                if(map.containsKey(query_id))
                    map.get(query_id).add(words[1]);
                else
                    map.put(query_id,new ArrayList<>());
                
                if(previous_query != query_id && previous_query !=0)
                {
                    
                    ret.put(previous_query, tot_documents);
                    tot_documents = 0;
                }
                tot_documents++;
                previous_query = query_id;
            } 
            
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", args[0]);      
        }
        ret.put(previous_query, tot_documents);
        query_id = 0;
        previous_query = 0;
        int intersec = 0;
                
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            String line;
            // read each document (line)
            while ((line = reader.readLine()) != null)
            {
                String[] words = line.split(" ");
               
                query_id = Integer.valueOf(words[0]);
                if(previous_query != query_id && previous_query !=0)
                {
                    
                    relret.put(previous_query,intersec);
                    relnumbermap.put(previous_query,relevant_number);
                    precision_five.put(previous_query,documents_rel_five);
                    precision_twenty.put(previous_query,documents_rel_twenty);
                    precision_hundred.put(previous_query,documents_rel_hundred);
                    intersec = 0;
                    relevant_number = 0;
                    documents_rel_five = 0;
                    documents_rel_twenty = 0;
                    documents_rel_hundred = 0;
                }
                
                if(Integer.parseInt(words[2]) == 1)
                {
                    relevant_number++;
                    if(map.get(query_id).contains(words[1]))
                    {
                        if(map.get(query_id).indexOf(words[1]) <= 4)
                        {
                            documents_rel_five++;
                        }
                        else if(map.get(query_id).indexOf(words[1]) <= 19)
                        {
                            documents_rel_twenty++;
                        }
                        else if(map.get(query_id).indexOf(words[1]) <= 99)
                        {
                            documents_rel_hundred++;
                        }
                        
                        intersec++;
                    }
                }
                previous_query = query_id;
            } 
            
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", args[0]);      
        }
        
        relret.put(previous_query,intersec);
        relnumbermap.put(previous_query,relevant_number);
        precision_five.put(previous_query,documents_rel_five);
        precision_twenty.put(previous_query,documents_rel_twenty);
        precision_hundred.put(previous_query,documents_rel_hundred);
        
        System.out.println("------------- PRECISION METRIC -----------------");
        float average_precision = 0.0f;
        for (Map.Entry<Integer, Integer> entry : relret.entrySet())
        {
            int key = entry.getKey();
            int numerator = entry.getValue();
            int denominator = ret.get(key);
            float precision = (float) numerator/denominator;
            
            average_precision += precision;
            System.out.println("The Precision value for the query number "+key +" is : "+df.format(precision));
        }
        System.out.println("The Average Precision value is : "+average_precision/relret.size());
        
        
        System.out.println("------------- PRECISION AT 5 METRIC -----------------");
        float average_p5 = 0.0f;
        for (Map.Entry<Integer, Integer> entry : precision_five.entrySet())
        {
            int key = entry.getKey();
            int numerator = entry.getValue();
            float value = (float)numerator/5;           
            average_p5 += value ;
            System.out.println("The Precision at 5 value for the query number "+key +" is : "+df.format(value) );
        }
        System.out.println("The Average Precision at 5 value is : "+df.format(average_p5/precision_five.size()));
        
        System.out.println("------------- PRECISION AT 20 METRIC -----------------");
        float average_p20 = 0.0f;
        for (Map.Entry<Integer, Integer> entry : precision_twenty.entrySet())
        {
            int key = entry.getKey();
            int numerator = entry.getValue();
            float value = (float)numerator/20;
            
            
            average_p20 += value ;
            System.out.println("The Precision at 20 value for the query number "+key +" is : "+df.format(value));
        }
        System.out.println("The Average Precision at 20 value is : "+df.format(average_p20/precision_twenty.size()));
        
        System.out.println("------------- PRECISION AT 100 METRIC -----------------");
        float average_p100 = 0.0f;
        for (Map.Entry<Integer, Integer> entry : precision_hundred.entrySet())
        {
            int key = entry.getKey();
            int numerator = entry.getValue();
            float value = (float) numerator/100;
            
            
            average_p100 +=value;
            System.out.println("The Precision at 100 value for the query number "+key +" is : "+df.format(value));
        }
        System.out.println("The Average Precision at 100 value is : "+df.format(average_p100/precision_hundred.size()));
        
        
        System.out.println("------------- RECALL METRIC -----------------");
        float average_recall = 0.0f;
        for (Map.Entry<Integer, Integer> entry : relret.entrySet())
        {
            int key = entry.getKey();
            int numerator = entry.getValue();
            int denominator = relnumbermap.get(key);
            
            float recall = (float) numerator/denominator;
            
            recall = (Float.isNaN(recall)?0.0f:recall);
           
            average_recall += recall ;
            System.out.println("The Recall value for the query number "+key +" is : "+df.format(recall));
        }
        System.out.println("The Average Recall value is : "+df.format(average_recall/relret.size()));
        
    }
}


