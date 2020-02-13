package com.lng.service;

import com.lng.pojo.FeedBack;
import org.springframework.data.domain.Page;

public interface FeedBackService {
    /**
     *
     * @description 添加修改意见反馈
     * @author zdf
     * @Version : 1.0
     * @ModifiedBy :
     * @date  2020年2月8日 下午4:49:21
     * @param feedBack 意见反馈实体
     * @return
     */
    public Integer saveOrUpdate(FeedBack feedBack);
    /**
     *
     * @description 根据主键获取意见反馈信息
     * @author zdf
     * @Version : 1.0
     * @ModifiedBy :
     * @date  2020年2月8日 下午4:49:47
     * @param id 主键
     * @return
     */
    FeedBack getEntityById(Integer id);

    /**
     *
     * @description 根据条件分页获取意见反馈信息
     * @author zdf
     * @Version : 1.0
     * @ModifiedBy :
     * @date  2020年2月8日  下午4:52:19
     * @param  readStatus 已读状态
     * @param pageNo 第几页
     * @param pageSize 每页多少条
     * @return
     */
    Page<FeedBack> getFeedBackByOption(Integer readStatus, String sDate, String eDate, Integer pageNo, Integer pageSize);
}
