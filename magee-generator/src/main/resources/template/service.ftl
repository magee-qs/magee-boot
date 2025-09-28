package ${packageName}.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${packageName}.domain.${entityName};
import ${packageName}.mapper.${entityName}Mapper;
import ${packageName}.service.I${entityName}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
* @author magee
* @description  ${tableName}(${comment}) 的数据库操作Service实现
* @version: v1.0
*/
@Service
public class ${entityName}Service extends ServiceImpl<${entityName}Mapper,${entityName}>
implements I${entityName}Service {
}