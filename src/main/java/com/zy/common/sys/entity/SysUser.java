package com.zy.common.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * 
 * @author wings
 * 
 */
public class SysUser implements  Serializable {
  private static final long serialVersionUID = 1L;
//  @Pattern(regexp = "\\w{3,30}", message = "账号必须由3-30位字母、数字或下划线")
  @Length(min = 2, max = 30, message = "请输入2-30字以内的字符")
  private String userId;// 账号
  private String userPwd;// 密码
  @Length(min = 2, max = 16, message = "请输入2-16字以内的汉字")
  private String userName;// 姓名
  private String userPy;// 姓名拼音
  @NotNull(message = "用户机构不能为空")
  private String userOrg;// 用户类别
  @Range(min = 0, max = 9, message = "性别不正确")
  private Short sex;// 性别
  @Length(max = 300, message = "邮箱最长300个字符")
  private String email;// 有些
  @Pattern(regexp = "^(((\\d{3,4}-{0,1}){0,1}\\d{7,8}(-{0,1}\\d{3,4}){0,1})|(1[3|4|5|7|8]\\d{9}))$", message = "电话号码格式错误")
  private String mobilePhone;// 手机号
  @Range(min = 0, max = 3, message = "状态值不正确")
  private Integer state;// 状态 sys_code.code_type=1
  @Length(max = 100, message = "备注最长100个字")
  private String remarks;// 备注
  @JsonIgnore
  private Date gmtCreate;// 创建时间
  private Date gmtUpdate;// 最后更新时间
  @JsonIgnore
  private Date gmtLogin;// 最后登陆时间
  private String orgName;// sys_user_class表对应的className
  private Integer delFlag;
  private String userType;
  private String certType;
  private String certNo;
  @JsonSerialize(using = ToStringSerializer.class)
  private Long infoId;
  private Integer relaUsertype;
  private String grandOrg;// 所属顶级机构
  private String secGrandOrgName;// 所属二级机构
  private String orgCodePath;// 机构全路径，用,分割，如：0,1,1-59,1-59-djxt-1,1-59-djxt-1-3
  // 查询条件，是否显示下级人员  1：是，其它：否
  private Boolean showLowerLevel = true;
  private Boolean showExtendOrg = false;
  // 扩展组织机构编码
  private String extendOrgCode;

  public Boolean getShowExtendOrg() {
    return showExtendOrg;
  }

  public void setShowExtendOrg(Boolean showExtendOrg) {
    this.showExtendOrg = showExtendOrg;
  }

  public String getExtendOrgCode() {
    return extendOrgCode;
  }

  public void setExtendOrgCode(String extendOrgCode) {
    this.extendOrgCode = extendOrgCode;
  }

  public Boolean getShowLowerLevel() {
    return showLowerLevel;
  }

  public void setShowLowerLevel(Boolean showLowerLevel) {
    this.showLowerLevel = showLowerLevel;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }

  public String getCertType() {
    return certType;
  }

  public void setCertType(String certType) {
    this.certType = certType;
  }

  public String getCertNo() {
    return certNo;
  }

  public void setCertNo(String certNo) {
    this.certNo = certNo;
  }

  public Integer getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(Integer delFlag) {
    this.delFlag = delFlag;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserPwd() {
    return userPwd;
  }

  public void setUserPwd(String userPwd) {
    this.userPwd = userPwd;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserPy() {
    return userPy;
  }

  public void setUserPy(String userPy) {
    this.userPy = userPy;
  }

  public String getUserOrg() {
    return userOrg;
  }

  public void setUserOrg(String userOrg) {
    this.userOrg = userOrg;
  }

  public Short getSex() {
    return sex;
  }

  public void setSex(Short sex) {
    this.sex = sex;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtUpdate() {
    return gmtUpdate;
  }

  public void setGmtUpdate(Date gmtUpdate) {
    this.gmtUpdate = gmtUpdate;
  }

  public Date getGmtLogin() {
    return gmtLogin;
  }

  public void setGmtLogin(Date gmtLogin) {
    this.gmtLogin = gmtLogin;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public Long getInfoId() {
    return infoId;
  }

  public void setInfoId(Long infoId) {
    this.infoId = infoId;
  }

  public Integer getRelaUsertype() {
    return relaUsertype;
  }

  public void setRelaUsertype(Integer relaUsertype) {
    this.relaUsertype = relaUsertype;
  }

  public String getGrandOrg() {
    return grandOrg;
  }

  public void setGrandOrg(String grandOrg) {
    this.grandOrg = grandOrg;
  }

  public String getSecGrandOrgName() {
    return secGrandOrgName;
  }

  public void setSecGrandOrgName(String secGrandOrgName) {
    this.secGrandOrgName = secGrandOrgName;
  }

  public String getOrgCodePath() {
    return orgCodePath;
  }

  public void setOrgCodePath(String orgCodePath) {
    this.orgCodePath = orgCodePath;
  }
}
