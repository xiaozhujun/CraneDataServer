package com.csei.portcrane.web;

import com.csei.crane.data.analysis.PinYu;
import com.csei.portcrane.domain.Message;
import com.csei.portcrane.service.DeviceDataService;
import com.csei.portcrane.service.SensorDataService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;

@Controller
@RequestMapping(value = "/sensors", method = RequestMethod.GET)
public class DeviceDataController {
    @Autowired
    private DeviceDataService service;

    @Autowired
    private SensorDataService sensorDataService;

    @RequestMapping(value = "/sensor/{id}.htm")
    @ResponseBody
    public String getSensorData(HttpServletRequest request,HttpServletResponse response,@PathVariable("id") String id){
        Set<Message> set = service.getMsgBySensor(id);
        return getJsonp(set,request.getParameter("callback"));
    }

    @RequestMapping(value = "/sensor/{id}/{time}.htm")
    @ResponseBody
    public String getSensorTimeData(HttpServletRequest request,HttpServletResponse response,@PathVariable("id") String id,@PathVariable("time") String time){
        SortedSet<Message> msg = service.getMsgBySensorAndTime(id,Long.parseLong(time));
        return getJsonp(msg,request.getParameter("callback"));
    }

    private String getJsonp(Object obj,String prefix){
        ObjectMapper mapper = new ObjectMapper();
        String result = prefix + "(";
        try {
            result += mapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result + ")";
    }

    @RequestMapping(value = "/sensor/data/{id}.htm")
    @ResponseBody
    public String getSensorDataArray(HttpServletRequest request,HttpServletResponse response,@PathVariable("id") String id){
        ArrayList dataArray = sensorDataService.getCurrentSensorDataArray(id);
        HashMap map = new HashMap();
        map.put("data",dataArray);
        return getJsonp(map,request.getParameter("callback"));
    }

    @RequestMapping(value = "/sensor/data/{analysis}/{id}.htm")
    @ResponseBody
    public String getSensorDataArrayAnalysis(HttpServletRequest request,HttpServletResponse response,@PathVariable("id") String id,@PathVariable("analysis") String analysisType){
        ArrayList dataArray = sensorDataService.getCurrentSensorDataArray(id);
        float[] data = getDataArray(dataArray);
        analysisData(data,analysisType);
        HashMap map = new HashMap();
        map.put("data",data);
        return getJsonp(map,request.getParameter("callback"));
    }

    //将数组列表转换为数据对象
    private float[] getDataArray(ArrayList dataArray){
        if(dataArray==null){
            return new float[0];
        }
        float[] result = new float[dataArray.size()];
        for(int i=0;i<dataArray.size();i++){
            result[i] = ((Double)dataArray.get(i)).floatValue();
        }
        return result;
    }

    //针对不同的数学方法进行分析
    private void analysisData(float[] data,String analysisType){
        if(analysisType.equals("fft")){
            PinYu.FFT(data);
        }
    }

}
