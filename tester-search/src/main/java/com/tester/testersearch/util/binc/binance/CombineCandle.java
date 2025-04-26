package com.tester.testersearch.util.binc.binance;

import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.util.BarEnum;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class CombineCandle {


    public static List<TradeSignDTO> combine(TradeSignDTO start, BarEnum barEnum, List<TradeSignDTO> list){
        List<TradeSignDTO> res = new ArrayList<>();
        if(CollectionUtils.isEmpty(list)){
            return res;
        }
        TradeSignDTO current = null;
        if(null != start){
            current = start;
            res.add(current);
        }
        for (TradeSignDTO item : list) {
            if(null != current && item.getId()<=current.getEndTimestamp()){
                current.setClose(item.getClose());
                current.setLastUpdateTimestamp(item.getId());
                if(item.getHigh().compareTo(current.getHigh()) > 0){
                    current.setHigh(item.getHigh());
                }
                if(item.getLow().compareTo(current.getLow()) < 0){
                    current.setLow(item.getLow());
                }
            }else{
                current = new TradeSignDTO();
                current.setOpenTimestamp(item.getId())
                        .setEndTimestamp(item.getId()+(barEnum.getInterval()*1000)-1)
                        .setLastUpdateTimestamp(item.getId())
                        .setTimestamp(item.getId())
                        .setOpen(item.getOpen())
                        .setClose(item.getClose())
                        .setHigh(item.getHigh())
                        .setLow(item.getLow())
                        .setBKey(item.getBKey())
                        .setBar(barEnum.getCode())
                        .setId(item.getId());
                res.add(current);
            }
        }
        return res;
    }
}
