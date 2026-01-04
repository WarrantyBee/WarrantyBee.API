package com.warrantybee.api.controllers;

import com.warrantybee.api.annotations.RequireSecurity;
import com.warrantybee.api.annotations.RolePermission;
import com.warrantybee.api.dto.internal.VendorContact;
import com.warrantybee.api.dto.request.VendorLoginCreationRequest;
import com.warrantybee.api.dto.response.APIResponse;
import com.warrantybee.api.dto.response.BaseResponse;
import com.warrantybee.api.enumerations.SecurityPermission;
import com.warrantybee.api.enumerations.SecurityRole;
import com.warrantybee.api.services.interfaces.IVendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendors")
public class VendorsController {
    private final IVendorService _service;

    public VendorsController(IVendorService service) {
        this._service = service;
    }

    @RequireSecurity({
        @RolePermission(role = SecurityRole.SUPER_ADMIN, permissions = { SecurityPermission.CREATE_VENDOR_LOGIN }),
    })
    @PostMapping("/{vendorId}/logins")
    public ResponseEntity<APIResponse<BaseResponse>> createVendorLogin(@PathVariable Long vendorId, VendorLoginCreationRequest request) {
        Long id = _service.createVendorLogin(vendorId, request);
        return ResponseEntity.ok(new APIResponse<>(new BaseResponse(id), null));
    }

    @RequireSecurity({
        @RolePermission(role = SecurityRole.SUPER_ADMIN, permissions = { SecurityPermission.ADD_VENDOR_CONTACT }),
        @RolePermission(role = SecurityRole.VENDOR, permissions = { SecurityPermission.ADD_VENDOR_CONTACT })
    })
    @PostMapping("/contacts")
    public ResponseEntity<APIResponse<BaseResponse>> addContact(VendorContact contact) {
        Long id = _service.addContact(contact);
        return ResponseEntity.ok(new APIResponse<>(new BaseResponse(id), null));
    }

    @RequireSecurity({
        @RolePermission(role = SecurityRole.SUPER_ADMIN, permissions = { SecurityPermission.UPDATE_VENDOR_CONTACT }),
        @RolePermission(role = SecurityRole.VENDOR, permissions = { SecurityPermission.UPDATE_VENDOR_CONTACT })
    })
    @PatchMapping("/contacts/{contactId}")
    public ResponseEntity<APIResponse<?>> updateContact(@PathVariable Long contactId, VendorContact contact) {
        _service.updateContact(contactId, contact);
        return ResponseEntity.ok(new APIResponse<>(null, null));
    }

    @RequireSecurity({
        @RolePermission(role = SecurityRole.SUPER_ADMIN, permissions = { SecurityPermission.REMOVE_VENDOR_CONTACT }),
        @RolePermission(role = SecurityRole.VENDOR, permissions = { SecurityPermission.REMOVE_VENDOR_CONTACT })
    })
    @DeleteMapping("/contacts/{contactId}")
    public ResponseEntity<APIResponse<?>> removeContact(@PathVariable Long contactId) {
        _service.removeContact(contactId);
        return ResponseEntity.ok(new APIResponse<>(null, null));
    }
}
