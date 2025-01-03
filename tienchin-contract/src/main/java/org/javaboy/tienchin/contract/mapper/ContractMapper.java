package org.javaboy.tienchin.contract.mapper;

import org.javaboy.tienchin.contract.domain.Contract;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.javaboy.tienchin.contract.domain.vo.ContractInfo;

import java.util.List;

/**
 * <p>
 * 合同表 Mapper 接口
 * </p>
 *
 * @author javaboy
 * @since 2024-12-31
 */
public interface ContractMapper extends BaseMapper<Contract> {

    List<ContractInfo> getContractInfoByPhone(String phone);
}
