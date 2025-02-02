import React, {useEffect, useState} from "react";
import {Dialog, DialogTitle, DialogContent, Button, TextField, Box, IconButton, Typography} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import CustomDialogTitle from "./CustomDialogTitle";
import CustomTextField from "./CustomTextField";

const ChangePasswordDialog = ({ open, onClose, onSubmit }) => {
  const [formData, setFormData] = useState({
    password: "",
    newPassword: "",
    confirmNewPassword: "",
  });

  useEffect(() => {
    setFormData({
      password: "",
      newPassword: "",
      confirmNewPassword: "",
    })
  }, [open]);

  const [error, setError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    if (formData.newPassword !== formData.confirmNewPassword) {
      setError("Mật khẩu mới và xác nhận mật khẩu không khớp.");
      return;
    }

    setError(""); // Clear previous error
    onSubmit({ password: formData.password, newPassword: formData.newPassword });
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
          position: "relative", // Để làm gốc cho nút X
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
      <CustomDialogTitle>Đổi Mật Khẩu</CustomDialogTitle>

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
          <CustomTextField
            label="Mật khẩu cũ"
            name="password"
            type="password"
            value={formData.password}
            onChange={handleChange}
            fullWidth
            required
          />
          <CustomTextField
            label="Mật khẩu mới"
            name="newPassword"
            type="password"
            value={formData.newPassword}
            onChange={handleChange}
            fullWidth
            required
          />
          <CustomTextField
            label="Xác nhận mật khẩu mới"
            name="confirmNewPassword"
            type="password"
            value={formData.confirmNewPassword}
            onChange={handleChange}
            fullWidth
            required
          />

          {/* Hiển thị lỗi nếu mật khẩu không khớp */}
          {error && (
            <Typography color="error" variant="body2">
              {error}
            </Typography>
          )}
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

export default ChangePasswordDialog;
