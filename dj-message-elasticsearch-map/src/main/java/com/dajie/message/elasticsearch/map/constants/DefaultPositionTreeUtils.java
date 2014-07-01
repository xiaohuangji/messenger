package com.dajie.message.elasticsearch.map.constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

import com.dajie.message.elasticsearch.map.constants.model.DefaultPositionTreeNode;
import com.dajie.message.elasticsearch.map.dao.IRegionInfoObjectDAO;
import com.dajie.message.elasticsearch.map.utils.LoggerManager;
import com.dajie.message.elasticsearch.map.utils.spring.MyApplicationContextUtil;
import com.dajie.message.model.map.RegionInfoObject;

public class DefaultPositionTreeUtils {
	
	private static Logger logger = LoggerManager.getLogger(DefaultPositionTreeUtils.class);
			
	private static DefaultPositionTreeNode treeNode;
	
	private static Set<DefaultPositionTreeNode> citySet;
	
	private static ApplicationContext applicationContext = null;
	
	private static IRegionInfoObjectDAO regionInfoObjectDAO = null;
	
	static
	{
		applicationContext = MyApplicationContextUtil.getContext();
		
		regionInfoObjectDAO = (IRegionInfoObjectDAO) applicationContext.getBean("regionInfoObjectDAO");
	}
	
	private DefaultPositionTreeUtils(){}
	
	private static byte[] lock = new byte[1];
	
	public static DefaultPositionTreeNode getInstance()
	{
		if (null == treeNode) {
		synchronized (lock) {
			if (null == treeNode) {
					treeNode = construct(1);
				}
			}
		}
		
		return treeNode;
	}

	public static Set<DefaultPositionTreeNode> getCitySet()
	{
		if (null == citySet) {
		synchronized (lock) {
			if (null == citySet) {
				getInstance();
				citySet = new HashSet<DefaultPositionTreeNode>();
				generalCitySet(treeNode);
			}
		}
	}
	
	return citySet;
		
	}
	
	private static DefaultPositionTreeNode construct(int id)
	{
		if(regionInfoObjectDAO == null)
		{
			logger.error("regionInfoObjectDAO is null");
			return null;
		}
		
		List<RegionInfoObject> parentNode = regionInfoObjectDAO.getByParentId(id);
		
		RegionInfoObject regionInfoObject = regionInfoObjectDAO.select(id);
		
		if(regionInfoObject == null) return null;
		
		DefaultPositionTreeNode node = new DefaultPositionTreeNode();
		
		node.setRegionInfoObject(regionInfoObject);
		
		List<DefaultPositionTreeNode> subNodes = new ArrayList<DefaultPositionTreeNode>();
		
		if(parentNode!=null&&parentNode.size()>0)
		{
			for(RegionInfoObject r : parentNode)
			{
				DefaultPositionTreeNode d = construct(r.getId());
				
				if(d!=null&&d.getRegionInfoObject().getId()!=0)
				{
					subNodes.add(d);
				}else
				{
					continue;
				}
			}
			node.setNodes(subNodes);
		}
		return node;
	}
	
	
	public static DefaultPositionTreeNode getById(DefaultPositionTreeNode node,int id)
	{
		
		if(node == null ) return null;
		
		if(node.getRegionInfoObject().getId() == id)
		{
			return node;
		}
		
		if(node!=null&&node.getNodes()!=null&&node.getNodes().size()>0)
		{
			for(DefaultPositionTreeNode d : node.getNodes())
			{
				DefaultPositionTreeNode defaultPositionTreeNode  = getById(d,id);
				if(defaultPositionTreeNode!=null) return defaultPositionTreeNode;
			}
		}
		
		return null;
	}
	
	public static DefaultPositionTreeNode getByName(DefaultPositionTreeNode node,String regionName)
	{
		
		if(node == null ) return null;
		
		if(node.getRegionInfoObject().getRegionName().equals(regionName))
		{
			return node;
		}
		
		if(node!=null&&node.getNodes()!=null&&node.getNodes().size()>0)
		{
			for(DefaultPositionTreeNode d : node.getNodes())
			{
				DefaultPositionTreeNode defaultPositionTreeNode  = getByName(d,regionName);
				if(defaultPositionTreeNode!=null) return defaultPositionTreeNode;
			}
		}
		
		return null;
	}
	
	private static void generalCitySet(DefaultPositionTreeNode node)
	{
		
		if(node == null ) return;
		
		if(node.getRegionInfoObject().getRegionName().endsWith("市")&&
				StringUtils.isNotEmpty(node.getRegionInfoObject().getRegionUniqueId()))
		{
			citySet.add(node);
		}
		
		if(node!=null&&node.getNodes()!=null&&node.getNodes().size()>0)
		{
			for(DefaultPositionTreeNode d : node.getNodes())
			{
				generalCitySet(d);
			}
		}
		
		return;
	}

}
