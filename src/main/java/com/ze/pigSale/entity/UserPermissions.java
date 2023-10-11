/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ze.pigSale.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.ze.pigSale.enums.PermissionEnum;
import lombok.Data;

/**
 * @author ze
 * @TableName user_permissions
 */
@Data
public class UserPermissions implements Serializable {

    private Long id;

    private Long userId;

    private PermissionEnum permissionId;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private static final long serialVersionUID = 1L;
}