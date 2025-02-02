import React, {useEffect, useState} from "react";
import { Dialog, DialogTitle, DialogContent, Button, TextField, Box, IconButton } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import CustomDialogTitle from "./CustomDialogTitle";
import CustomTextField from "./CustomTextField";

const UpdateTaskDialog = ({ open, onClose, onSubmit, oldData }) => {
  const [formData, setFormData] = useState({
    name: oldData?.name || "",
    description: oldData?.description || "",
  });

  useEffect(() => {
    if (open) {
      setFormData({
        name: oldData?.name || "",
        description: oldData?.description || "",
      });
    }
  }, [open]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    onSubmit(formData);
    onClose(); // Đóng form sau khi submit
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
      <CustomDialogTitle>Cập nhật công việc</CustomDialogTitle>


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
            label="Tên công việc"
            name="name"
            value={formData.name}
            onChange={handleChange}
            fullWidth
            required
          />
          <CustomTextField
            label="Mô Tả"
            name="description"
            value={formData.description}
            onChange={handleChange}
            fullWidth
            multiline
            rows={3}
          />
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

export default UpdateTaskDialog;
