import React, { useEffect, useState } from "react";
import {
  Dialog,
  DialogContent,
  Button,
  Box,
  IconButton,
  InputLabel,
  FormControl,
  Select,
  MenuItem,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import CustomDialogTitle from "./CustomDialogTitle";
import { getAllTaskStatus } from "../services/taskService";
import { useSnackbar } from "../context/SnackbarContext";

const ChangeTaskStatusDialog = ({ open, onClose, currentStatus, onSubmit }) => {
  const [statusList, setStatusList] = useState([]);
  const [selectedStatus, setSelectedStatus] = useState(currentStatus || "");
  const { showSnackbar } = useSnackbar();

  const statusTranslation = {
    "TODO": "Chưa bắt đầu",
    "IN_PROCESSING": "Đang thực hiện",
    "COMPLETED": "Đã hoàn thành",
  };

  useEffect(() => {
    if (!open) return;

    setSelectedStatus(currentStatus || "");

    const fetchTaskStatus = async () => {
      getAllTaskStatus()
        .then((response) => {
          setStatusList(
            response?.data?.result.map((status) => ({
              id: status.name,
              label: statusTranslation[status.name] || status.name,
              value: status.name,
            }))
          );
        })
        .catch((error) => {
          showSnackbar(error?.response?.data?.message, "error");
        });
    };
    fetchTaskStatus();
  }, [open]);

  const handleChange = (e) => {
    setSelectedStatus(e.target.value);
  };

  const handleSubmit = () => {
    onSubmit({ status: selectedStatus });
    onClose();
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
      <CustomDialogTitle>Thay Đổi Trạng Thái Công Việc</CustomDialogTitle>

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
          <FormControl fullWidth required>
            <InputLabel id="status-select-label">Trạng thái</InputLabel>
            <Select
              labelId="status-select-label"
              id="status-select"
              value={selectedStatus}
              label="Trạng thái"
              onChange={handleChange}
              sx={{
                fontSize: 15,
                fontFamily: "'Google Sans', sans-serif",
              }}
            >
              {statusList.map((status) => (
                <MenuItem
                  key={status.value}
                  value={status.value}
                  sx={{
                    fontSize: 15,
                    fontFamily: "'Google Sans', sans-serif",
                  }}
                >
                  {status.label}
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

export default ChangeTaskStatusDialog;
