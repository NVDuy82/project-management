import React from "react";
import { Dialog, DialogActions, DialogContent, DialogTitle, Button, Typography } from "@mui/material";

const ConfirmDialog = ({ open, onClose, onConfirm, title, message }) => {
  return (
    <Dialog
      open={open}
      onClose={(event, reason) => {
        if (reason !== "backdropClick") {
          onClose();
        }
      }}
      PaperProps={{
        sx: {
          borderRadius: 3,
        },
      }}
    >
      <DialogTitle>{title}</DialogTitle>
      <DialogContent>
        <Typography>{message}</Typography>
      </DialogContent>
      <DialogActions>
        <Button
          onClick={onClose}
          color="primary"
          sx={{
            borderRadius: 20, // Thêm borderRadius cho nút Hủy
            padding: "6px 20px", // Thêm padding cho nút Hủy
            backgroundColor: "#ecf5ff",
            color: "#005488",
            '&:hover': {
              backgroundColor: "#bfe5ff", // Màu nền khi hover
              color: "#004977",
            }
          }}
        >
          Hủy
        </Button>
        <Button
          onClick={onConfirm}
          color="secondary"
          sx={{
            borderRadius: 20, // Thêm borderRadius cho nút Xác nhận
            padding: "6px 20px", // Thêm padding cho nút Xác nhận
            backgroundColor: "#f44336", // Màu đỏ cho nút Xác nhận
            color: "#ffffff",
            '&:hover': {
              backgroundColor: "#d32f2f", // Màu nền đỏ đậm khi hover
              color: "#ffffff",
            }
          }}
        >
          Xác nhận
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ConfirmDialog;
