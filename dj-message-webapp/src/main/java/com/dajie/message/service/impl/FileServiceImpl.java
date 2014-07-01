package com.dajie.message.service.impl;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dajie.common.file.enums.FileSavedType;
import com.dajie.common.file.model.UploadReturnModel;
import com.dajie.common.file.service.FileUploadService;
import com.dajie.message.constants.returncode.CommonResultCode;
import com.dajie.message.mcp.model.MCPResponse;
import com.dajie.message.mcp.model.UserPassport;
import com.dajie.message.mcp.service.IPassportService;
import com.dajie.message.model.user.UploadResult;
import com.dajie.message.util.log.JsonMapping;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller("IFileService")
public class FileServiceImpl {

	private static Logger logger = LoggerFactory
			.getLogger(FileServiceImpl.class);

	private static final int ERROR_TOKEN = 1;

	private static final int ERROR_EMPTY_FILE = 2;

	private static final int PHP_UPLOAD_SUCC = 2;

	@Autowired
	private IPassportService passportService;

	@RequestMapping(value = "/IFileService/upload")
	@ResponseBody
	public String upload(@RequestParam("_t") String token,
			@RequestParam("type") String type,
			@RequestParam("fileName") String fileName,
			@RequestParam("file") MultipartFile file) {

		MCPResponse resp = new MCPResponse();
		resp.setCode(CommonResultCode.OP_FAIL);
		int intType = NumberUtils.toInt(type, 0);
		if (intType > 0) {
			if (StringUtils.isEmpty(token))
				resp.setCode(ERROR_TOKEN);
			if (file.isEmpty())
				resp.setCode(ERROR_EMPTY_FILE);
			if (!token.equals("test")) {
				UserPassport passport = passportService
						.getPassportByTicket(token);
				if (passport == null || passport.getUserId() == 0
						|| StringUtils.isEmpty(passport.getTicket())
						|| !passport.getTicket().equals(token)) {
					resp.setCode(ERROR_TOKEN);
				}
			}

			try {
				UploadReturnModel returnModel = null;
				switch (intType) {
				case 2: // 既保存原图，也需要缩略图
					returnModel = FileUploadService.uploadFromBytes(
							file.getBytes(), fileName,
							FileSavedType.seduce_picture);
					if (returnModel != null
							&& returnModel.getStatus() == PHP_UPLOAD_SUCC) {
						resp.setCode(CommonResultCode.OP_SUCC);
						UploadResult urls = new UploadResult();
						urls.setFileUrl(getRealUrl(returnModel.getLocalUrl(),
								"c"));
						urls.setSmallImgUrl(getRealUrl(
								returnModel.getLocalUrl(), "l"));
						resp.setRet(urls);
					} else {
						resp.setCode(CommonResultCode.OP_FAIL);
					}
					break;
				case 3: // 聊天语音
					returnModel = FileUploadService.uploadFromBytes(
							file.getBytes(), fileName,
							FileSavedType.seduce_voice);
					logger.info("voice type, fileName:" + fileName);
					if (returnModel != null
							&& returnModel.getStatus() == PHP_UPLOAD_SUCC) {
						resp.setCode(CommonResultCode.OP_SUCC);
						UploadResult url = new UploadResult();
						url.setFileUrl(returnModel.getLocalUrl());
						resp.setRet(url);
					} else {
						resp.setCode(CommonResultCode.OP_FAIL);
					}
					break;
				default:
					returnModel = FileUploadService.uploadFromBytes(
							file.getBytes(), fileName, FileSavedType.file);
					if (returnModel != null
							&& returnModel.getStatus() == PHP_UPLOAD_SUCC) {
						resp.setCode(CommonResultCode.OP_SUCC);
						UploadResult url = new UploadResult();
						url.setFileUrl(returnModel.getLocalUrl());
						resp.setRet(url);
					} else {
						resp.setCode(CommonResultCode.OP_FAIL);
					}
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				resp.setCode(CommonResultCode.OP_FAIL);

			}
		}
		String s = "";
		try {
			s = JsonMapping.getMapper().writeValueAsString(resp);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
		}
		return s;
	}

	@RequestMapping(value = "/IFileService/uploadWithOutToken")
	@ResponseBody
	public String uploadWithOutToken(@RequestParam("type") String type,
			@RequestParam("fileName") String fileName,
			@RequestParam("file") MultipartFile file) {

		MCPResponse resp = new MCPResponse();
		resp.setCode(-1);
		int intType = NumberUtils.toInt(type, 0);
		if (intType == 1) {
			if (file.isEmpty())
				resp.setCode(ERROR_EMPTY_FILE);
			try {
				UploadReturnModel returnModel = FileUploadService
						.uploadFromBytes(file.getBytes(), fileName,
								FileSavedType.file);
				if (returnModel != null
						&& returnModel.getStatus() == PHP_UPLOAD_SUCC) {
					resp.setCode(CommonResultCode.OP_SUCC);
					UploadResult url = new UploadResult();
					url.setFileUrl(returnModel.getLocalUrl());
					resp.setRet(url);
				} else {
					resp.setCode(CommonResultCode.OP_FAIL);
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				resp.setCode(CommonResultCode.OP_FAIL);

			}
		}
		String s = "";
		try {
			s = JsonMapping.getMapper().writeValueAsString(resp);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
		}
		return s;
	}

	private String getRealUrl(String localUrl, String rep) {
		int index = localUrl.lastIndexOf('.');
		return localUrl.substring(0, index) + rep
				+ localUrl.substring(index, localUrl.length());
	}

}
