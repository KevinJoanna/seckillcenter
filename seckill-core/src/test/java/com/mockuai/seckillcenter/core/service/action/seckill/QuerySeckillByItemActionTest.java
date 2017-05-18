package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.common.constant.ActionEnum;
import com.mockuai.seckillcenter.core.BaseActionTest;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by edgar.zr on 12/15/15.
 */
public class QuerySeckillByItemActionTest extends BaseActionTest {

	public QuerySeckillByItemActionTest() {
		super(QuerySeckillByItemActionTest.class.getName());
	}

	@Override
	protected String getCommand() {
		return ActionEnum.QUERY_SECKILL_BY_ITEM.getActionName();
	}

	@Test
	public void test() {
		List<Long> skuIds = Arrays.asList(
//                1261L,1264L,
				66271L
//                ,1349L,1350L,1352L,1353L,1358L,1359L,1360L,1361L,1364L,1365L
		);
//        Long skuId = 1365L;
		Long sellerId = 38699L;
		for (Long skuId : skuIds) {
			request.setParam("skuId", skuId);
			request.setParam("sellerId", sellerId);

			doExecute();
		}
	}
}