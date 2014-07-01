package com.dajie.message.elasticsearch.map.constants.model;

import java.util.List;

import com.dajie.message.model.map.RegionInfoObject;

public class DefaultPositionTreeNode {
	
	private RegionInfoObject regionInfoObject;
	
	private List<DefaultPositionTreeNode> nodes;

	public RegionInfoObject getRegionInfoObject() {
		return regionInfoObject;
	}

	public void setRegionInfoObject(RegionInfoObject regionInfoObject) {
		this.regionInfoObject = regionInfoObject;
	}

	public List<DefaultPositionTreeNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<DefaultPositionTreeNode> nodes) {
		this.nodes = nodes;
	}
	
	
}
