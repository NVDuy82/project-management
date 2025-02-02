import React from "react";
import { Box, Typography } from "@mui/material";

const InfoLineTaskDetail = ({
                          label,
                          value,
                          labelFontSize = 14,
                          valueFontSize = 14,
                          labelFontWeight = 600,
                          labelColor = "black",
                          valueColor = "black",
                        }) => {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
        width: "100%",
        gap: "10px",
        height: "40px",
      }}
    >
      <Typography
        sx={{
          fontSize: labelFontSize,
          fontWeight: labelFontWeight,
          color: labelColor,
          fontFamily: "'Google Sans', sans-serif",
        }}
      >
        {label}
      </Typography>

      <Typography
        sx={{
          fontSize: valueFontSize,
          color: valueColor,
          maxWidth: "300px",
          overflow: "hidden",
          textOverflow: "ellipsis",
          whiteSpace: "nowrap",
          fontFamily: "'Google Sans', sans-serif",
        }}
      >
        {value}
      </Typography>
    </Box>
  );
};

export default InfoLineTaskDetail;
