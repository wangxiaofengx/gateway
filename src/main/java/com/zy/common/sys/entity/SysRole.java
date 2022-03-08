package com.zy.common.sys.entity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class SysRole implements Serializable {
  private static final long serialVersionUID = 1L;
  // 用户角色
  @NotNull
  private String sysId;// 系统id
  private String orgCode;//创建机构
  private String orgName;// 创建机构名称
  @NotNull
  private String roleId;// 角色id
  @NotNull
  private String roleName;// 角色名称
  private String remarks;// 角色备注
  private Integer lvl;
  private Integer lft;
  private Integer rgt;

  public String getSysId() {
    return sysId;
  }

  public void setSysId(String sysId) {
    this.sysId = sysId;
  }
  
  public String getOrgCode() {
    return orgCode;
  }
  
  public void setOrgCode(String orgCode) {
    this.orgCode = orgCode;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public Integer getLvl() {
    return lvl;
  }

  public void setLvl(Integer lvl) {
    this.lvl = lvl;
  }

  public Integer getLft() {
    return lft;
  }

  public void setLft(Integer lft) {
    this.lft = lft;
  }

  public Integer getRgt() {
    return rgt;
  }

  public void setRgt(Integer rgt) {
    this.rgt = rgt;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }
  
  
}
