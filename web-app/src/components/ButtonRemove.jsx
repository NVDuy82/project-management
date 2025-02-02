import React from "react";
import { styled } from "@mui/system";
import { Button } from "@mui/material";
import DeleteOutlineIcon from "@mui/icons-material/DeleteOutline";

const CustomButton = styled(Button)(({ theme }) => ({
  padding: "0px", // Kích thước padding phù hợp
  borderRadius: 8, // Bo góc mềm mại
  fontSize: "1.0rem",
  fontFamily: "'Google Sans', sans-serif",
  textTransform: "none", // Giữ nguyên kiểu chữ
  backgroundColor: "#d6d6d6", // Màu nền xám nhạt
  color: "#4a4a4a", // Màu chữ xám đậm
  boxShadow: "none", // Không có hiệu ứng bóng
  height: "50px",
  "&:hover": {
    backgroundColor: "#b0b0b0", // Màu hover xám đậm hơn
    color: "#3a3a3a",
  },

  "& .MuiButton-startIcon": {
    color: "#4a4a4a", // Màu icon
    margin: 0, // Loại bỏ margin của icon
  },
}));

const ButtonRemove = ({ children, onClick, ...props }) => {
  return (
    <CustomButton onClick={onClick} startIcon={<DeleteOutlineIcon  />} {...props}>
      {children} {/* Text truyền vào */}
    </CustomButton>
  );
};

export default ButtonRemove;
