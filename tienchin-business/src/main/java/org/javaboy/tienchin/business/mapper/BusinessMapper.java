package org.javaboy.tienchin.business.mapper;

import org.javaboy.tienchin.business.domain.Business;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.javaboy.tienchin.business.domain.vo.BusinessSummary;
import org.javaboy.tienchin.business.domain.vo.BusinessVO;
import org.javaboy.tienchin.common.core.domain.model.EchartPoint;

import java.util.List;

/**
 * <p>
 * 商机表 Mapper 接口
 * </p>
 *
 * @author javaboy
 * @since 2024-12-30
 */
public interface BusinessMapper extends BaseMapper<Business> {

    List<BusinessSummary> selectBusinessList(BusinessVO businessVO);

    List<EchartPoint> increaseBusiness(BusinessVO businessVO);

    List<EchartPoint> totalBusiness(BusinessVO businessVO);
}
