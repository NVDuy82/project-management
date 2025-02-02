import React, { useState } from "react";
import { Box, Typography, Button, TextField } from "@mui/material";
import IconButton from "@mui/material/IconButton";
import EditIcon from "@mui/icons-material/Edit";
import CheckIcon from "@mui/icons-material/Check";
import CloseIcon from "@mui/icons-material/Close";

const InfoLine = ({
                    label,
                    value,
                    allowEdit = true,
                    onSubmit,
                    labelFontSize = 14,
                    valueFontSize = 14,
                    labelFontWeight = 600,
                    labelColor = "black",
                    valueColor = "black",
                    errorMessage = "",
                    isEditing = false,
                  }) => {
  const [newValue, setNewValue] = useState(value); // To hold the edited value
  const [isInEditMode, setIsInEditMode] = useState(isEditing); // To control edit mode
  const [localError, setLocalError] = useState(errorMessage); // Error state

  const handleEditClick = () => {
    setIsInEditMode(true); // Switch to edit mode
    setNewValue(value); // Reset to the original value
    setLocalError(""); // Clear any existing error
  };

  const handleCancelClick = () => {
    setIsInEditMode(false); // Exit edit mode
    setNewValue(value); // Reset to the original value
    setLocalError(""); // Clear any error
  };

  const handleSaveClick = async () => {
    if (!onSubmit) {
      setLocalError("An error occurred!"); // Handle error
      return;
    }

    const result = await onSubmit(newValue); // Submit the new value to the parent component or API
    console.log(result);
    if (result?.success) {
      setIsInEditMode(false); // Exit edit mode
    } else {
      setLocalError(result ? (result.error || "An error occurred!") : "An error occurred!"); // Handle error
    }
  };

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

      <Box sx={{ display: "flex", flexDirection: "row", alignItems: "center", gap: "10px" }}>
        {isInEditMode ? (
          <>
            <TextField
              value={newValue}
              onChange={(e) => setNewValue(e.target.value)} // Update the input field
              size="small"
              sx={{
                width: "200px",
                fontFamily: "'Google Sans', sans-serif",
              }}
              inputProps={{
                style: {
                  fontSize: valueFontSize,
                  textAlign: "right", // Căn lề phải
                },
              }}
              error={!!localError} // Hiển thị lỗi nếu `localError` có giá trị
              helperText={localError} // Hiển thị thông báo lỗi ở dòng dưới
            />
            <Box
              sx={{
                width: "68px",
                display: "flex",
                justifyContent: "flex-end",
                alignItems: "center",
              }}
            >
              {allowEdit && (
                <>
                  <IconButton onClick={handleSaveClick} size="small">
                    <CheckIcon sx={{ color: "blue" }} />
                  </IconButton>
                  <IconButton onClick={handleCancelClick} size="small">
                    <CloseIcon sx={{ color: "red" }} />
                  </IconButton>
                </>
              )}
            </Box>
          </>
        ) : (
          <>
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
            <Box
              sx={{
                width: "68px",
                display: "flex",
                justifyContent: "flex-end",
                alignItems: "center",
              }}
            >
              {allowEdit && (
                <IconButton onClick={handleEditClick} size="small">
                  <EditIcon />
                </IconButton>
              )}
            </Box>
          </>
        )}
      </Box>
    </Box>
  );
};

export default InfoLine;
