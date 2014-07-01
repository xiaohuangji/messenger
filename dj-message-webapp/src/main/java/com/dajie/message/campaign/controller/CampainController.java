package com.dajie.message.campaign.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dajie.message.elasticsearch.map.utils.LoggerManager;
import com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchCampaignUtil;
import com.dajie.message.model.campaign.CampaignInfoModel;
import com.dajie.message.model.campaign.LotteryModel;
import com.dajie.message.model.map.campaign.CampaignChanceModel;
import com.dajie.message.model.map.campaign.UserCampaignRecordsModel;
import com.dajie.message.service.IPersonMapObjectService;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
@RequestMapping("/campaign")
public class CampainController {
	
	private static Logger logger = LoggerManager.getLogger(CampainController.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	IPersonMapObjectService personMapObjectService;
	
	
	@RequestMapping("showCampaignPage")
	public ModelAndView getCampaignPage(@RequestParam(required = false)Integer userId,@RequestParam(required = false)String campaignModelId)
	{
		 ModelAndView mv=new ModelAndView("showCampaignPage");
		 
		 if(StringUtils.isEmpty(campaignModelId))
		 {
			 logger.error("get no campaignModelId while getting campaign.");
			 ModelAndView emv=new ModelAndView("errorCampaignPage");
			 emv.addObject("errorMessage", "no requested params!");
			 return emv;
		 }else
		 {
			 
			 CampaignChanceModel campaignChanceModel = ElasticSearchCampaignUtil.getCampaignChanceModelById(campaignModelId);
			 
			 if(campaignChanceModel==null)
			 {
				 logger.error("campaign is not exist.");
				 ModelAndView emv=new ModelAndView("errorCampaignPage");
				 emv.addObject("errorMessage", "机会已过期或不存在。");
				 return emv;
			 }
			 
			 int campaignId = campaignChanceModel.getCampaignId();
			 
			 int isOnline = campaignId % 2;
			 
			 if(isOnline <= 0)
			 {
				 logger.error("campaign is not online.");
				 ModelAndView emv=new ModelAndView("errorCampaignPage");
				 emv.addObject("errorMessage", "活动已下线。");
				 return emv;
			 }
			 
			 
			 if(userId == null||userId == 0)
			 {
				 logger.warn("get no userId while getting campaign.");
			 }
			 else
			 {
				 if(userId != campaignChanceModel.getUserId())
				 {
					 logger.warn("user "+userId+"wants to get user "+campaignChanceModel.getUserId()+
							 "'s campaign chance for campaign id "+campaignChanceModel.getCampaignId());
				 }
				 
				 mv.addObject("campaignModelId", campaignChanceModel.get_id());
			 }
		 }
		 
		 return mv;
	}
	
	
	@RequestMapping("useCampaignChance")
	public ModelAndView useCampaignChance(@RequestParam(required = false)Integer userId,@RequestParam(required = false)String campaignModelId)
	{
		 ModelAndView mv=new ModelAndView("showCampaignPage");
		 
		 if(StringUtils.isEmpty(campaignModelId))
		 {
			 logger.error("get no campaignModelId while getting campaign.");
			 ModelAndView emv=new ModelAndView("errorCampaignPage");
			 emv.addObject("errorMessage", "no requested params!");
			 emv.addObject("status", -1);
			 return emv;
		 }else
		 {
			 
			 CampaignChanceModel campaignChanceModel = ElasticSearchCampaignUtil.getCampaignChanceModelById(campaignModelId);
			 
			 if(campaignChanceModel==null)
			 {
				 logger.error("campaign is not exist.");
				 ModelAndView emv=new ModelAndView("errorCampaignPage");
				 emv.addObject("errorMessage", "机会已过期或不存在。");
				 emv.addObject("status", -1);
				 return emv;
			 }
			 
			 int campaignId = campaignChanceModel.getCampaignId();
			 
			 //用id获取活动详情
			 CampaignInfoModel campaignInfoModel = new CampaignInfoModel();
			 campaignInfoModel.setId(campaignId);
			 campaignInfoModel.setFreeChancesCount(10);
			 
			 
			 int isOnline = campaignInfoModel.getIsOnline();
			 
			 if(isOnline <= 0)
			 {
				 logger.error("campaign is not online.");
				 ModelAndView emv=new ModelAndView("errorCampaignPage");
				 emv.addObject("errorMessage", "活动已下线。");
				 emv.addObject("status", -1);
				 return emv;
			 }
			 
			 
			 if(userId == null||userId == 0)
			 {
				 logger.warn("get no userId while getting campaign.");
			 }
			 else
			 {
				 if(userId != campaignChanceModel.getUserId())
				 {
					 logger.warn("user "+userId+"wants to get user "+campaignChanceModel.getUserId()+
							 "'s campaign chance for campaign id "+campaignChanceModel.getCampaignId());
				 }
				 
				 //利用用户id和campaignId从数据库取出用户的记录，判断是否还有抽奖次数
				 //如果不存在需要新创建一条,并将抽奖次数置为活动免费抽奖次数
				 userId = campaignChanceModel.getUserId();
				 
				 UserCampaignRecordsModel userCampaignRecordsModel = null;
				 
				 if(userCampaignRecordsModel == null)
				 {
					 userCampaignRecordsModel =  new UserCampaignRecordsModel();
					 userCampaignRecordsModel.setCampaignId(campaignChanceModel.getCampaignId());
					 userCampaignRecordsModel.setPointsBalance(0);
					 userCampaignRecordsModel.setChancesCount(campaignInfoModel.getFreeChancesCount());
				 }
				 
				 
				 
				 if(userCampaignRecordsModel.getChancesCount()<1)
				 {
					 ModelAndView emv=new ModelAndView("errorCampaignPage");
					 emv.addObject("errorMessage", "您已没有抽奖机会。");
					 emv.addObject("status", -1);
					 return emv;
				 }
				 
				 //开始抽奖
				 
				 int result = ElasticSearchCampaignUtil.getResultOfLottery(campaignChanceModel.getLotteryRate());
				 
				 if(result > 0)
				 {
					 mv.addObject("message", "您中奖了。奖品如下。");
					 mv.addObject("status", 1);
					 
					 //从数据库随机选择一个奖品
					 LotteryModel lotteryModel = new LotteryModel();
					 
					 lotteryModel.setExpireTime(System.currentTimeMillis());
					 lotteryModel.setExchangeUrl("你看到了一个兑换地址");
					 lotteryModel.setDescription("你看到了一串中奖信息描述");
					 lotteryModel.setAmount(100);
					 lotteryModel.setType(1);
					 lotteryModel.setName("现金兑换卡");
					 
					 mv.addObject("amount",lotteryModel.getAmount());
					 mv.addObject("message", lotteryModel.getDescription());
					 mv.addObject("exchangeUrl", lotteryModel.getDescription());
					 mv.addObject("name", lotteryModel.getName());
					 
					 //将奖品数据库状态置为已经使用并更新数据库
					 lotteryModel.setIsUsed(1);
					 
					 //给用户的账户点数上涨中奖的点数
					 double points = userCampaignRecordsModel.getPointsBalance() + lotteryModel.getAmount();
					 userCampaignRecordsModel.setPointsBalance(points);
					 
				 }else
				 {
					 mv.addObject("message", "您没中奖。");
					 mv.addObject("status", 0);
				 }
				 
				 //删除本次抽奖使用的机会
				 ElasticSearchCampaignUtil.deleteCampaignChanceModelById(campaignChanceModel.get_id());
				 
				 //减少一次抽奖次数 更新最终抽奖时间 并更新数据库 
				 int count = userCampaignRecordsModel.getChancesCount();
				 count--;
				 userCampaignRecordsModel.setChancesCount(count);
				 userCampaignRecordsModel.setLastJoinTime(System.currentTimeMillis());
			 }
		 }
		 
		 return mv;
	}
	


	public IPersonMapObjectService getPersonMapObjectService() {
		return personMapObjectService;
	}


	public void setPersonMapObjectService(
			IPersonMapObjectService personMapObjectService) {
		this.personMapObjectService = personMapObjectService;
	}
	
}
