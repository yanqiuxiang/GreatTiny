package com.yqx.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.github.pagehelper.page.PageMethod;
import com.yqx.entity.Y_User;
import com.yqx.model.JqgridBean;
import com.yqx.model.PageRusult;

@SuppressWarnings("all")
public class JsonUtil {
	
	private static LinkedHashMap<Integer, Object> errorMap = new LinkedHashMap<Integer, Object>();
	static {
		errorMap.put(0, "");
		errorMap.put(1, "系统异常");
		errorMap.put(2, "无法找到该记录");
		errorMap.put(3, "当前信息已存在");
		errorMap.put(4, "未获取选中记录，请重新选择");
		errorMap.put(5, "缺少相关信息");
	} 
	
	//删除成功
	public static LinkedHashMap<String, Object> operationSuccessMap() {
		LinkedHashMap<String, Object> resultmap = new LinkedHashMap<String, Object>(){{
			put("state", "success");
			put("mesg", "操作成功");
		}};
		return resultmap;
	}
	
	//删除异常
	public static LinkedHashMap<String, Object> operationErrorMap(final Integer errorType) {
		LinkedHashMap<String, Object> resultmap = new LinkedHashMap<String, Object>(){{
			put("state", "fail");
			put("mesg", "操作失败,"+errorMap.get(errorType).toString().trim());
		}};
		return resultmap;
	}
	
	//整合条件查询及分页
	public static  void searPage(JqgridBean jqgridbean,Criteria criteria, Example example, String searChOper, String eqName) {
		setSear(jqgridbean, criteria, example, searChOper, eqName);
		setPage(jqgridbean);
	}
	
	//返回数据
	public static <T> LinkedHashMap<String, Object> getPageRusult(List<T> list) {
		
		LinkedHashMap<String, Object> resultmap = new LinkedHashMap<String, Object>();
		PageRusult<T> pageRusult = new PageRusult<T>(list);
        resultmap.put("currpage", String.valueOf(pageRusult.getPageNum()));
        resultmap.put("totalpages", String.valueOf(pageRusult.getPages()));
        resultmap.put("totalrecords", String.valueOf(pageRusult.getTotal()));
        resultmap.put("datamap", list);
		return resultmap;
	}
	
	//条件查询
	private static void setSear(JqgridBean jqgridbean,Criteria criteria, Example example, String searChOper, String eqName) {
		
		if (StringUtils.isNotEmpty(jqgridbean.getSearchString())) {
            if (eqName.equalsIgnoreCase(jqgridbean.getSearchField())) {
                if (searChOper.contentEquals(jqgridbean.getSearchOper())) {
                    criteria.andEqualTo(eqName,jqgridbean.getSearchString());
                }
            }
        }
        if(StringUtils.isNotEmpty(jqgridbean.getSidx())&&StringUtils.isNotEmpty(jqgridbean.getSord())){
        	example.setOrderByClause(jqgridbean.getSidx() + " " + jqgridbean.getSord());
        }
	}
	
	//分页
	private static void setPage(JqgridBean jqgridbean) {
		
		PageMethod.startPage(jqgridbean.getPage(), jqgridbean.getLength());
	}
	
	
	public static void main(String[] args) {
		System.out.println(operationErrorMap(2));
	}
}
