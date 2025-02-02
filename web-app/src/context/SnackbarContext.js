import React, { createContext, useContext, useState, useCallback } from "react";
import { Snackbar, Alert } from "@mui/material";

const SnackbarContext = createContext();

export const SnackbarProvider = ({ children }) => {
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: "",
    color: "info", // Màu mặc định
    duration: 3000, // Thời gian mặc định
  });

  const showSnackbar = useCallback((message, color = "info", duration = 3000) => {
    setSnackbar({ open: true, message, color, duration });
  }, []);

  const closeSnackbar = () => {
    setSnackbar((prev) => ({ ...prev, open: false }));
  };

  return (
    <SnackbarContext.Provider value={{ showSnackbar }}>
      {children}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={snackbar.duration} // Dùng duration từ state
        onClose={closeSnackbar}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
      >
        <Alert
          onClose={closeSnackbar}
          severity={snackbar.color} // Màu sắc dựa trên `color`
          sx={{
            bgcolor:
              snackbar.color === "success"
                ? "green"
                : snackbar.color === "error"
                  ? "red"
                  : snackbar.color === "info"
                    ? "white"
                    : "inherit",
            color: snackbar.color === "info" ? "black" : "white",
          }}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </SnackbarContext.Provider>
  );
};

export const useSnackbar = () => useContext(SnackbarContext);
