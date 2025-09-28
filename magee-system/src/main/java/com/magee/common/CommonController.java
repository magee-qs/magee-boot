package com.magee.common;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.common.config.SystemConfig;
import com.magee.common.utils.DateUtils;
import com.magee.common.utils.ObjectUtils;
import com.magee.common.utils.StringUtils;
import com.magee.common.utils.file.FileUploadUtils;
import com.magee.common.utils.file.FileUtils;
import com.magee.framework.config.ApplicationConfig;
import com.magee.framework.config.shiro.Anonymous;
import com.magee.framework.core.vo.AjaxResult;
import com.magee.system.domain.*;
import com.magee.system.query.UserParam;
import com.magee.system.service.IBaseCommonAPI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/common")
@Api(tags = "通用功能")
public class CommonController {
    @Autowired
    private IBaseCommonAPI commonAPI;

    /**
     * 通用文件上传
     * */
    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile file){
        if(file.isEmpty()){
            return AjaxResult.error("没有上传文件");
        }
        try{
            String filePath = SystemConfig.getUploadPath() + "/" + DateUtils.dateTime();
            String fileName = FileUploadUtils.upload(filePath, file);
            AjaxResult result = AjaxResult.ok();
            result.put("fileName", file.getName());
            result.put("filePath", fileName);
            return result;
        }catch (Exception ex){
            return AjaxResult.error(ex.getMessage());
        }
    }

    /**
     * 文件下载
     * */
    @GetMapping("/download")
    public void download(String fileName, Boolean delete, HttpServletResponse response){
        try{
            if(FileUtils.checkAllowDownload(fileName)){
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String filePath = SystemConfig.getDownloadPath() + fileName;
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }catch (Exception e){
            log.error("下载文件失败,fileName:{}, deleted: {}, message: {}", fileName, delete, e.getMessage(), e);
        }
    }

    @ApiOperation("获取字典")
    @GetMapping("/getAllDictData")
    public AjaxResult getAllDictData(){
        List<SysDictData> data = commonAPI.getAllDictData();
        return AjaxResult.ok(data);
    }


    @ApiOperation("获取字典分类")
    @GetMapping("/getDictType")
    public AjaxResult getDictType(){
        List<SysDictType> list = commonAPI.getDictType();
        Object[] data = list.stream().filter(item -> ObjectUtils.equal(1, item.getStatus())).toArray();
        return  AjaxResult.ok(data);
    }

    @ApiOperation("获取用户列表")
    @GetMapping("/getUserList")
    public AjaxResult getUserList(UserParam userParam){
        IPage<SysUser> page = commonAPI.getUserList(userParam);
        return AjaxResult.list(page);
    }

    @ApiOperation("获取角色列表")
    @GetMapping("/getRoleList")
    public AjaxResult getRoleList(SysRole roleParam){
        IPage<SysRole> page = commonAPI.getRoleList(roleParam);
        return AjaxResult.list(page);
    }


    @ApiOperation("获取部门数据")
    @GetMapping("/getDepartList")
    public AjaxResult getDepartList(){
        Object[] arr = commonAPI.getDepartList().stream().filter(item -> ObjectUtils.equal(1, item.getStatus()))
                .toArray();
        return AjaxResult.ok(arr);
    }


}
