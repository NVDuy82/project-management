import React from 'react';
import { Typography } from '@mui/material';

const TitleTypography = ({ children, variant = 'h5', fontWeight = 'bold', height = 42, padding = '0px 20px' }) => {
  return (
    <Typography
      variant={variant}
      component="h1"
      fontWeight={fontWeight}
      sx={{
        padding: padding,
        height: height, // Chiều cao cố định
        display: 'flex', // Đặt display là flex
        alignItems: 'center', // Căn giữa theo chiều dọc
        justifyContent: 'center', // Căn giữa theo chiều ngang
        fontFamily: "'Google Sans', sans-serif",
      }}
    >
      {children}
    </Typography>
  );
};

export default TitleTypography;
