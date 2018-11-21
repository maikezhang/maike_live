package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.AnchorBlackWhiteMapper;
import cn.idongjia.live.db.mybatis.po.AnchorBlackWhitePO;
import cn.idongjia.live.db.mybatis.query.DBAnchorBlackWhiteQuery;
import cn.idongjia.live.restructure.dto.AnchorBlackWhiteDTO;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/2
 * Time: 下午2:11
 */
@Component
public class AnchorBlackWhiteRepo {

    @Resource
    private AnchorBlackWhiteMapper anchorBlackWhiteMapper;

    public Long add(AnchorBlackWhiteDTO anchorBlackWhiteDTO) {
        AnchorBlackWhitePO anchorBlackWhitePO = anchorBlackWhiteDTO.toDO();

        anchorBlackWhiteMapper.insert(anchorBlackWhitePO);

        return anchorBlackWhitePO.getId();

    }

    public void batchAdd(List<AnchorBlackWhiteDTO> dtos) {
        List<AnchorBlackWhitePO> pos = dtos.stream().map(anchorBlackWhiteDTO -> {
            return anchorBlackWhiteDTO.toDO();
        }).collect(Collectors.toList());

        anchorBlackWhiteMapper.batchInsert(pos);

    }

    public Integer update(AnchorBlackWhiteDTO dto) {

        AnchorBlackWhitePO anchorBlackWhitePO = dto.toDO();
        int update = anchorBlackWhiteMapper.update(anchorBlackWhitePO, Utils.getCurrentMillis());
        return update;

    }

    public List<AnchorBlackWhiteDTO> list(DBAnchorBlackWhiteQuery query) {

        List<AnchorBlackWhitePO> pos = anchorBlackWhiteMapper.list(query);

        List<AnchorBlackWhiteDTO> anchorBlackWhiteDTOS = pos.stream().map(AnchorBlackWhiteDTO::new).collect(Collectors.toList());

        return anchorBlackWhiteDTOS;


    }

    public boolean delete(Long id) {

        int delete = anchorBlackWhiteMapper.delete(id);
        return delete > 0;

    }

    public AnchorBlackWhiteDTO get(long anchorId){
        AnchorBlackWhitePO po = anchorBlackWhiteMapper.get(anchorId);
        if(Objects.isNull(po)){
            return null;
        }
        return new AnchorBlackWhiteDTO(po);
    }


}
