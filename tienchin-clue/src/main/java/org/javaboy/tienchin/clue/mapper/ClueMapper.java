package org.javaboy.tienchin.clue.mapper;

import org.javaboy.tienchin.clue.domain.Clue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.javaboy.tienchin.clue.domain.vo.ClueDetails;
import org.javaboy.tienchin.clue.domain.vo.ClueSummary;
import org.javaboy.tienchin.clue.domain.vo.ClueVO;

import java.util.List;

/**
 * <p>
 * 线索表 Mapper 接口
 * </p>
 *
 * @author javaboy
 * @since 2024-12-28
 */
public interface ClueMapper extends BaseMapper<Clue> {

    List<ClueSummary> selectClueList(ClueVO clueVO);

    ClueDetails getClueDetailsByClueId(Integer clueId);
}
