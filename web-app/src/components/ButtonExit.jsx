import React from "react";
import { styled } from "@mui/system";
import { Button } from "@mui/material";
import ExitToAppIcon from "@mui/icons-material/ExitToApp";

const CustomButton = styled(Button)(({ theme }) => ({
  padding: "0px", // Kích thước padding phù hợp
  borderRadius: 8, // Bo góc mềm mại
  fontSize: "1.0rem",
  fontFamily: "'Google Sans', sans-serif",
  textTransform: "none", // Giữ nguyên kiểu chữ
  backgroundColor: "#ffb2b2", // Màu nền đỏ nhạt
  color: "#8b0000", // Màu chữ đỏ đậm
  boxShadow: "none", // Không có hiệu ứng bóng
  height: "50px",
  "&:hover": {
    backgroundColor: "#ff6969", // Màu hover đỏ đậm hơn
    color: "#6b0000",
  },

  "& .MuiButton-startIcon": {
    color: "#8b0000", // Màu icon
    margin: 0, // Loại bỏ margin của icon
  },
}));

const ButtonExit = ({ children, onClick, ...props }) => {
  return (
    <CustomButton onClick={onClick} startIcon={<ExitToAppIcon />} {...props}>
      {children} {/* Text truyền vào */}
    </CustomButton>
  );
};

export default ButtonExit;
