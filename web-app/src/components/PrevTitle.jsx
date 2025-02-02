import React from "react";
import { styled } from "@mui/system";
import { Button } from "@mui/material";

const CustomButton = styled(Button)(({ theme }) => ({
  padding: "0px 20px", // Loại bỏ padding để giống như văn bản
  borderRadius: 20, // Loại bỏ bo góc
  fontSize: "1.5rem", // Tương đương với font-size của Typography variant h5
  fontFamily: "'Google Sans', sans-serif",
  textTransform: "none", // Loại bỏ việc tự động chuyển thành chữ hoa
  backgroundColor: "transparent", // Tắt màu nền
  color: "#5f6368", // Đảm bảo màu sắc giống văn bản
  boxShadow: "none", // Không có bóng
  "&:hover": {
    backgroundColor: "#E7E8EB",
    color: "#5f6368",
  },
  "&:focus": {
    backgroundColor: "#E7E8EB",
    outline: "none",
  },
}));

const PrevTitle = ({ children, onClick, ...props }) => {
  return (
    <CustomButton onClick={onClick} {...props}>
      {children}
    </CustomButton>
  );
};

export default PrevTitle;
