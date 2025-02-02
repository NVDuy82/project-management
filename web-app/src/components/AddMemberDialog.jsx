import React, {useEffect, useState} from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  Button,
  Box,
  IconButton,
  InputLabel,
  FormControl,
  Select,
  MenuItem,
  CircularProgress, Autocomplete, TextField,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import CustomDialogTitle from "./CustomDialogTitle";
import CustomTextField from "./CustomTextField";
import {getAllTaskStatus} from "../services/taskService";
import {useSnackbar} from "../context/SnackbarContext";
import {getAllProjectRole, searchMember} from "../services/projectService";
import {searchUser} from "../services/userService";

const AddMemberDialog = ({ open, onClose, projectId, onSubmit }) => {
  const [formData, setFormData] = useState({
    projectId: projectId,
    userId: "",
    role: "",
  });

  const roleTranslation = {
    "MANAGER": "Quản lý",
    "MEMBER": "Thành viên",
    "VIEWER": "Người theo dõi",
  };

  const { showSnackbar } = useSnackbar();

  const [userOptions, setUserOptions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [roleList, setRoleList] = useState([]);

  useEffect(() => {
    if (!open) return;

    setFormData({
      projectId: projectId,
      userId: "",
      role: "",
    });

    const fetchProjectRole = async () => {
      getAllProjectRole()
        .then((response) => {
          setRoleList(
            response?.data?.result
              .filter((role) => role?.name !== "MANAGER")
              .map((role) => ({
                id: role.name,
                label: roleTranslation[role.name] || role.name,
                value: role.name,
              }))
          );
        })
        .catch((error) => {
          showSnackbar(error?.response?.data?.message, "error");
        });
    };
    fetchProjectRole();
  }, [open]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    if (formData.assignedUserId === "") {
      formData.assignedUserId = null;
    }

    onSubmit(formData);
    onClose();
  };

  // Gọi API để tìm kiếm người dùng
  const handleSearchUser = async (query) => {
    if (!query) {
      setUserOptions([]);
      return;
    }
    setLoading(true);

    searchUser(query)
      .then((response) => {
        setUserOptions(
          response?.data?.result
            .map((profile) => ({
              id: profile?.userId,
              label: `${profile?.fullName} (${profile?.username}) (${profile?.email})`,
              value: profile?.userId,
            }))
        );
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      })
      .finally(() => {
        setLoading(false);
      });
  };

  return (
    <Dialog
      open={open}
      onClose={(event, reason) => {
        if (reason !== "backdropClick") {
          onClose();
        }
      }}
      maxWidth="sm"
      fullWidth
      PaperProps={{
        sx: {
          padding: "50px 16px 16px 16px",
          borderRadius: 3,
          position: "relative",
        },
      }}
    >
      {/* Nút X */}
      <IconButton
        onClick={onClose}
        sx={{
          position: "absolute",
          top: 20,
          right: 20,
          color: "grey.500",
          backgroundColor: "#E4E6EB",
          "&:hover": {
            backgroundColor: "#dee0e6",
          },
        }}
      >
        <CloseIcon />
      </IconButton>

      {/* Tiêu đề */}
      <CustomDialogTitle>Thêm thành viên</CustomDialogTitle>

      {/* Nội dung */}
      <DialogContent>
        <Box
          display="flex"
          flexDirection="column"
          gap={2}
          sx={{
            paddingTop: 1,
          }}
        >
          {/* Thanh tìm kiếm người dùng */}
          <Autocomplete
            options={userOptions}
            loading={loading}
            onInputChange={(event, newInputValue) => handleSearchUser(newInputValue)}
            onChange={(event, newValue) => {
              setFormData((prev) => ({
                ...prev,
                userId: newValue?.id || "",
              }));
            }}
            renderInput={(params) => (
              <CustomTextField
                {...params}
                label="Người phụ trách"
                placeholder="Tìm người dùng theo tên hoặc email"
                fullWidth
                InputProps={{
                  ...params.InputProps,
                  endAdornment: (
                    <>
                      {loading ? <CircularProgress color="inherit" size={20} /> : null}
                      {params.InputProps.endAdornment}
                    </>
                  ),
                }}
              />
            )}
          />

          <FormControl fullWidth required>
            <InputLabel id="status-select-label">Vai trò</InputLabel>
            <Select
              labelId="role-select-label"
              id="role-select"
              name="role"
              value={formData.role}
              label="Vai trò"
              onChange={handleChange}
              sx={{
                fontSize: 15,
                fontFamily: "'Google Sans', sans-serif",
              }}
            >
              {roleList.map((role) => (
                <MenuItem
                  key={role.id}
                  value={role.value}
                  sx={{
                    fontSize: 15,
                    fontFamily: "'Google Sans', sans-serif",
                  }}
                >
                  {role.label}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Box>
        <Box
          sx={{
            display: "flex",
            justifyContent: "flex-end",
            gap: 2,
            paddingTop: 2,
          }}
        >
          <Button
            onClick={handleSubmit}
            variant="contained"
            color="primary"
            sx={{
              borderRadius: 30,
              fontFamily: "'Google Sans', sans-serif",
              fontWeight: "normal",
            }}
          >
            Xác Nhận
          </Button>
        </Box>
      </DialogContent>
    </Dialog>
  );
};

export default AddMemberDialog;
