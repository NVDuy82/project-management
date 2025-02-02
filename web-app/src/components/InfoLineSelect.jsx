import React, { useState } from "react";
import { Box, Typography, IconButton, Select, MenuItem } from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import CheckIcon from "@mui/icons-material/Check";
import CloseIcon from "@mui/icons-material/Close";

const InfoLineSelect = ({
                          label,
                          value,
                          options, // Danh sách các giá trị có thể chọn
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
  const [newValue, setNewValue] = useState(value);
  const [isInEditMode, setIsInEditMode] = useState(isEditing);
  const [localError, setLocalError] = useState(errorMessage);

  const handleEditClick = () => {
    setIsInEditMode(true);
    setNewValue(value);
    setLocalError("");
  };

  const handleCancelClick = () => {
    setIsInEditMode(false);
    setNewValue(value);
    setLocalError("");
  };

  const handleSaveClick = async () => {
    if (!onSubmit) return;

    const result = await onSubmit(newValue);
    if (result?.success) {
      setIsInEditMode(false);
    } else {
      setLocalError(result?.error || "An error occurred!");
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
            <Select
              value={newValue}
              onChange={(e) => setNewValue(e.target.value)}
              size="small"
              sx={{
                width: "200px",
                fontSize: valueFontSize,
                fontFamily: "'Google Sans', sans-serif",
              }}
              error={!!localError}
            >
              {options.map((option) => (
                <MenuItem
                  key={option.value}
                  value={option.value}
                  sx={{
                    fontSize: valueFontSize,
                    fontFamily: "'Google Sans', sans-serif",
                  }}
                >
                  {option.label}
                </MenuItem>
              ))}
            </Select>
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
              {options.find((option) => option.value === value)?.label || "Không xác định"}
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

export default InfoLineSelect;
