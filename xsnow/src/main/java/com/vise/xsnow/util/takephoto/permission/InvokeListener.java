package com.vise.xsnow.util.takephoto.permission;


import com.vise.xsnow.util.takephoto.model.InvokeParam;

/**
 * 授权管理回调
 */
public interface InvokeListener {
    PermissionManager.TPermissionType invoke(InvokeParam invokeParam);
}
