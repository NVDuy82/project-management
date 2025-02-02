import React from "react";
import { styled } from "@mui/system";
import { Button } from "@mui/material";
import UpdateIcon from "@mui/icons-material/Update";

const CustomButton = styled(Button)(({ theme }) => ({
  padding: "0px", // Kích thước padding phù hợp
  borderRadius: 8, // Bo góc mềm mại
  fontSize: "1.0rem",
  fontFamily: "'Google Sans', sans-serif",
  textTransform: "none", // Giữ nguyên kiểu chữ
  backgroundColor: "#b2ffb2", // Màu nền xanh nhạt
  color: "#006400", // Màu chữ xanh đậm
  boxShadow: "none", // Không có hiệu ứng bóng
  height: "50px",
  "&:hover": {
    backgroundColor: "#69ff69", // Màu hover xanh đậm hơn
    color: "#004d00",
  },

  "& .MuiButton-startIcon": {
    color: "#006400", // Màu icon
    margin: 0, // Loại bỏ margin của icon
  },
}));

const ButtonUpdate = ({ children, onClick, ...props }) => {
  return (
    <CustomButton onClick={onClick} startIcon={<UpdateIcon  />} {...props}>
      {children} {/* Text truyền vào */}
    </CustomButton>
  );
};

export default ButtonUpdate;
