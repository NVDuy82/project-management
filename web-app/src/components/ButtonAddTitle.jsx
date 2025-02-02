import React from "react";
import { styled } from "@mui/system";
import { Button } from "@mui/material";
import AddIcon from "@mui/icons-material/Add"; // Thêm icon dấu +

const CustomButton = styled(Button)(({ theme }) => ({
  padding: "7px 12px", // Loại bỏ padding để giống như văn bản
  borderRadius: 8, // Bo góc
  fontSize: "1.0rem",
  fontFamily: "'Google Sans', sans-serif",
  textTransform: "none", // Loại bỏ việc tự động chuyển thành chữ hoa
  backgroundColor: "#b2d6ff",
  color: "#005488",
  boxShadow: "none", // Không có bóng
  "&:hover": {
    backgroundColor: "#69c3ff",
    color: "#004977",
  },

  "& .MuiButton-startIcon": {
    color: "#005488",
  },
}));

const ButtonAddTitle = ({ children, onClick, ...props }) => {
  return (
    <CustomButton onClick={onClick} startIcon={<AddIcon />} {...props}>
      {children} {/* Text truyền vào */}
    </CustomButton>
  );
};

export default ButtonAddTitle;
