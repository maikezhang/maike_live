package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.DisconfManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class WhiteListRepo {

	@Resource
	private DisconfManager disconfManager;
	@Resource
	private ConfigManager  configManager;
	@Resource
	private UserManager    userManager;

	private static final Log	LOGGER	= LogFactory.getLog(WhiteListRepo.class);

	/**
	 * 批量增加白名单主播ID
	 * 
	 * @param newUids
	 *            主播ID
	 * @return 是否成功
	 */
	public boolean addUids(List<Long> newUids) {
		Set<Long> whiteUids = listUidsAsSet();
		Set<Long> failedSet = new HashSet<>();
		LOGGER.info("修改之前白名单uids: " + whiteUids);
		for (Long uid : newUids) {
			try {
				userManager.getUser(uid);
				whiteUids.add(uid);
			} catch (LiveException e) {
				failedSet.add(uid);
			}
		}
		LOGGER.info("修改之后白名单uids: " + whiteUids);
		replaceWhiteList(whiteUids);
		if (failedSet.size() == 0) {
			return true;
		} else {
			throw new LiveException(-12138, "用户ID: " + StringUtils.join(failedSet, ",") + "不存在");
		}
	}

	/**
	 * 根据集合写入disconf
	 * 
	 * @param collection
	 *            主播ID集合
	 * @return 是否成功
	 */
	private boolean replaceWhiteList(Collection collection) {
		String uidStr = StringUtils.join(collection, ",");
		return disconfManager.addConfig(uidStr, configManager.getWhiteListConfName(), "whitelist_uids");
	}

	/**
	 * 集合形式获取白名单主播ID
	 * 
	 * @return 主播ID集合
	 */
	Set<Long> listUidsAsSet() {
		Set<Long> reSet = new HashSet<>();
		String uidStr = configManager.getWhiteListUids();
		if (uidStr == null || "".equals(uidStr)) {
			return reSet;
		}
		for (String uid : uidStr.split(",")) {
			reSet.add(Long.parseLong(uid));
		}
		return reSet;
	}

	/**
	 * 列表形式获取主播ID
	 * 
	 * @return 主播ID列表
	 */
	public List<Long> listUids() {
		List<Long> reList = new ArrayList<>();
		String uidStr = configManager.getWhiteListUids();
		if (uidStr == null || "".equals(uidStr)) {
			return reList;
		}
		for (String uid : uidStr.split(",")) {
			reList.add(Long.parseLong(uid));
		}
		return reList;
	}

	/**
	 * 删除白名单主播ID
	 * 
	 * @param uid
	 *            主播ID
	 * @return 是否成功
	 */
	public boolean deleteAnchor(Long uid) {
		Set<Long> uids = listUidsAsSet();
		LOGGER.info("删除前白名单: " + uids);
		if (uids.contains(uid)) {
			uids.remove(uid);
		}
		LOGGER.info("删除后白名单: " + uids);
		return replaceWhiteList(uids);
	}

	/**
	 * 增加主播ID
	 * 
	 * @param uid
	 *            主播ID
	 * @return 是否成功
	 */
	public boolean addAnchor(Long uid) {
		Set<Long> uids = listUidsAsSet();
		LOGGER.info("增加之前白名单: " + uids);
		try {
			userManager.getUser(uid);
			uids.add(uid);
		} catch (Exception e) {
			throw new LiveException(-12138, "用户ID: " + uid + "不存在");
		}
		LOGGER.info("增加之后白名单: " + uids);
		return replaceWhiteList(uids);
	}
}
