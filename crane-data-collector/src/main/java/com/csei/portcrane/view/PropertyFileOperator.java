package com.csei.portcrane.view;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PropertyFileOperator {
    public PropertyFileOperator() {
    }

    public Map<String,String> read(File file) throws IOException {
        Map<String,String> result = new HashMap<String, String>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        while(true){
            String line = br.readLine();
            if(line==null){
                break;
            }
            String[] ss = line.split("=");
            result.put(ss[0],ss[1]);
        }
        br.close();
        return result;
    }

    public void write(Map<String,String> map,File file) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        Set<String> keySet = map.keySet();
        Iterator<String> it = keySet.iterator();
        while(it.hasNext()){
            String key = it.next();
            String value = map.get(key);
            bw.write(key + "=" + value);
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }
}
