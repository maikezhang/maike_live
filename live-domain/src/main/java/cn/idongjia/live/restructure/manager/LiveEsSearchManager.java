package cn.idongjia.live.restructure.manager;

import cn.idongjia.essearch.lib.dto.EsResultDTO;
import cn.idongjia.essearch.lib.dto.live.LiveDTO;
import cn.idongjia.essearch.lib.query.live.LiveEsSearch;
import cn.idongjia.essearch.lib.service.live.LiveSearchService;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.restructure.dto.LiveSearch4HomepageDTO;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LiveEsSearchManager {

    private static final Log LOG=LogFactory.getLog(LiveEsSearchManager.class);

    @Resource
    LiveSearchService liveHomeSearchService;


    public BaseList<LiveSearch4HomepageDTO> homeSearch(LiveEsSearch liveEsSearch){
        BaseList<LiveSearch4HomepageDTO> baseList=new BaseList<>();
        EsResultDTO<List<LiveDTO>> esResultDTO= liveHomeSearchService.search(liveEsSearch);
        List<LiveDTO> liveDTOList= esResultDTO.getData();
        baseList.setCount(esResultDTO.getTotal().intValue());
        if(!Utils.isEmpty(liveDTOList)){
            List<LiveSearch4HomepageDTO> liveSearch4HomepageDTOS=  liveDTOList.stream().map(LiveSearch4HomepageDTO::new).collect(Collectors.toList());
            baseList.setItems(liveSearch4HomepageDTOS);
        }
        return baseList;
    }
}
